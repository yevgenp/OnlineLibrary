angular
  .module('olApp')
  .controller('ListBookController', ListBookController);

function ListBookController($scope, $rootScope, backend, BookService,
                  NgTableParams, toastr, $uibModal, $window, $translate) {
  var ctrl = this;
  ctrl.translations = {};
  ctrl.books = [];
  ctrl.accordion = {open: false};
  ctrl.search = {filters: {}};

  ctrl.getBooks = function (params) {
    var searchObj = {
      key: ctrl.search.key,
      author: ctrl.search.filters.author == ''? null: ctrl.search.filters.author,
      genre: ctrl.search.filters.genre == ''? null: ctrl.search.filters.genre
    };
    var page = params.page() - 1;
    var size = params.count();
    var sort = params.sorting();
    return BookService.getBooks(searchObj, page, size, sort)
      .then(ctrl.onBooksReceived, ctrl.showErrorToast);
  };

  ctrl.onBooksReceived = function (response) {
    ctrl.tableParams.total(response.data.page.totalElements);
    ctrl.books = response.data._embedded? response.data._embedded.books: [];
    $rootScope.session.id && BookService.getFavorites($rootScope.session.id)
      .then(ctrl.onFavoritesReceived);
    return ctrl.books;
  };

  ctrl.onFavoritesReceived = function (response) {
    var favorites = response.data._embedded.books;
    var favIds = {};
    favorites.forEach(function (book) {
      favIds[book.id] = true;
    });
    ctrl.books.forEach(function (book) {
      if (favIds[book.id])
        book.inFavorites = true;
    });
  };

  ctrl.showErrorToast = function () {
    var title = ctrl.translations['failure'];
    var text = ctrl.translations['fetchError'];
    var override = {preventDuplicates: true};
    toastr.error(text, title, override);
  };

  ctrl.tableParams = new NgTableParams(
    { sorting: {author: "asc"}},
    { getData: ctrl.getBooks }
    );

  $scope.$watch('ctrl.search', function () {
    ctrl.tableParams.reload();
  }, true);

  $scope.$on('books list updated', function () {
    ctrl.tableParams.reload();
    ctrl.updateFilters();
  });

  ctrl.updateFilters = function () {
    BookService.getAuthors("", 0, 10).then(ctrl.onAuthorsReceived);
    BookService.getGenres("", 0, 10).then(ctrl.onGenresReceived);
  };


  ctrl.switchAccordionState = function () {
    ctrl.accordion.open = !ctrl.accordion.open
  };

  $scope.$watch(function() {return ctrl.accordion.open}, function () {
    if (ctrl.accordion && ctrl.accordion.open) {
      ctrl.updateFilters();
    }
  });

  ctrl.onAuthorsReceived = function (response) {
    response.data.content.unshift('');
    ctrl.authors = response.data.content;
  };

  ctrl.onGenresReceived = function (response) {
    response.data.content.unshift('');
    ctrl.genres = response.data.content;
  };

  ctrl.addNewBook = function () {
      $uibModal.open({
        animation: true,
        ariaLabelledBy: 'Book addition',
        ariaDescribedBy: 'Add new book to library',
        templateUrl: 'app/components/book/add/add.book.html',
        controller: 'AddBookController',
        controllerAs: 'ctrl',
        scope: $scope,
        windowClass: "modal fade in"
      });
    };

  ctrl.download = function (book) {
      var url = backend + 'api/books/' + book.id + '/download';
      $window.open(url);
  };

  ctrl.addToFavorites = function (book) {
    BookService.addToFavorites($rootScope.session.id, book).then(function () {
      book.inFavorites = true;
    });
  };

  ctrl.remFromFavorites = function (book) {
    BookService.remFromFavorites($rootScope.session.id, book).then(function () {
      book.inFavorites = false;
    });
  };

  ctrl.delete = function (book) {
    BookService.delete(book).then(function () {
      $scope.$broadcast('books list updated');
    });
  };

  $rootScope.$on('$translateChangeSuccess', function() {ctrl.translate()});

  ctrl.translate = function () {
    $translate(['common.failure', 'book.fetchError', 'book.author',
      'book.title', 'book.genre', 'book.description', 'book.action'])
      .then(function (translations) {
        ctrl.translations['failure'] = translations['common.failure'];
        ctrl.translations['fetchError'] = translations['book.fetchError'];
        ctrl.translations['author'] = translations['book.author'];
        ctrl.translations['title'] = translations['book.title'];
        ctrl.translations['genre'] = translations['book.genre'];
        ctrl.translations['description'] = translations['book.description'];
        ctrl.translations['action'] = translations['book.action'];
      });
  };

  ctrl.translate();

}
