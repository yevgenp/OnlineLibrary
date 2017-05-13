describe('list book controller', function () {
  var ctrl, $scope, $rootScope, backend, BookService, $uibModal,
    defer, $q, toastr, $window, $translate, $timeout;

  module.sharedInjector();
  beforeAll(module('olApp'));
  beforeAll(inject(function (_$rootScope_, _$controller_, _backend_,
                             _$window_, _$q_, _NgTableParams_, _$timeout_) {
    $rootScope = _$rootScope_;
    $scope = _$rootScope_.$new();
    backend = _backend_;
    BookService = jasmine.createSpyObj('service',
      ['getBooks','getFavorites','getAuthors','getGenres','getGenres',
        'addToFavorites','remFromFavorites','delete']);
    $uibModal = jasmine.createSpyObj('modal', ['open']);
    $q = _$q_;
    defer = _$q_.defer();
    toastr = jasmine.createSpyObj('toastr', ['error']);
    $window =jasmine.createSpyObj('$window', ['open']);
    $timeout = _$timeout_;
    NgTableParams = _NgTableParams_;
    $translate = jasmine.createSpy('$translate').and.returnValue(defer.promise);
    ctrl = _$controller_('ListBookController', {
      $scope : $scope,
      $rootScope: $rootScope,
      backend: backend,
      BookService: BookService,
      NgTableParams: NgTableParams,
      toastr: toastr,
      $uibModal: $uibModal,
      $window: $window,
      $translate: $translate
    });
  }));

  it ('should have controller and initial values defined', function () {
    expect(ctrl).toBeDefined();
    expect(ctrl.books).toEqual([]);
    expect(ctrl.search).toEqual({filters: {}});
    expect(ctrl.tableParams).toBeDefined();
  });

  it ('should call service method for books', function () {
    BookService.getBooks.and.returnValue(defer.promise);
    ctrl.getBooks({page: function () {},
      count: function () {},
      sorting: function () {}});
    expect(BookService.getBooks).toHaveBeenCalled();
  });

  it('should set books property on response and call for favorites', function () {
    var response = {data: {
      page: {totalElements: 20},
      _embedded: {books: 'books'}
    }};
    $rootScope.session = {id: 3};
    BookService.getFavorites.and.returnValue(defer.promise);
    ctrl.onBooksReceived(response);
    expect(ctrl.books).toEqual(response.data._embedded.books);
    expect(BookService.getFavorites).toHaveBeenCalledWith($rootScope.session.id);
  });

  it('should process and mark favorites', function () {
    ctrl.books = [{id: 1},{id: 2},{id: 3}];
    var response = {data: {
      _embedded: {books: [{id:2},{id:3}]}
    }};
    ctrl.onFavoritesReceived(response);
    expect(ctrl.books[0].inFavorites).toBeFalsy();
    expect(ctrl.books[1].inFavorites).toBeTruthy();
    expect(ctrl.books[2].inFavorites).toBeTruthy();
  });

  it('should show toast on error', function () {
    ctrl.showErrorToast();
    expect(toastr.error).toHaveBeenCalled();
  });

  it('should call for books when search changed', function () {
    ctrl.tableParams = jasmine.createSpyObj('params', ['reload']);
    ctrl.search = 'new';
    $scope.$digest();
    expect(ctrl.tableParams.reload).toHaveBeenCalled();
  });

  it('should call for updates when books list updated', function () {
    ctrl.tableParams = jasmine.createSpyObj('params', ['reload']);
    spyOn(ctrl, 'updateFilters');
    $scope.$broadcast('books list updated');
    expect(ctrl.tableParams.reload).toHaveBeenCalled();
    expect(ctrl.updateFilters).toHaveBeenCalled();
  });

  it('should switch accordion state', function () {
    ctrl.accordion.open = false;
    ctrl.switchAccordionState();
    expect(ctrl.accordion.open).toBeTruthy();
  });

  it('should update filters', function () {
    ctrl.accordion.open = true;
    spyOn(ctrl, 'updateFilters');
    $scope.$digest();
    expect(ctrl.updateFilters).toHaveBeenCalled();
  });

  it('should call for authors and genres', function () {
    BookService.getAuthors.and.returnValue(defer.promise);
    BookService.getGenres.and.returnValue(defer.promise);
    ctrl.updateFilters();
    expect(BookService.getAuthors).toHaveBeenCalled();
    expect(BookService.getGenres).toHaveBeenCalled();
  });

  it('should set authors on response', function () {
    var response = {data:{content: ['content']}};
    ctrl.onAuthorsReceived(response);
    expect(ctrl.authors).toEqual(['', 'content']);
  });

  it('should set genres on response', function () {
    var response = {data:{content: ['content']}};
    ctrl.onGenresReceived(response);
    expect(ctrl.genres).toEqual(['', 'content']);
  });

  it('should open addBook modal', function () {
    ctrl.addNewBook();
    expect($uibModal.open).toHaveBeenCalled();
  });

  it('should download book file', function () {
    var book = {id:4};
    ctrl.download(book);
    expect($window.open).toHaveBeenCalledWith(backend + 'api/books/' + book.id + '/download');
  });

  it('should add to favorites', function () {
    var book = {id:4};
    $rootScope.session = {id: 1};
    var defer = $q.defer();
    BookService.addToFavorites.and.returnValue(defer.promise);
    defer.resolve();
    ctrl.addToFavorites(book);
    $scope.$digest();
    expect(BookService.addToFavorites).toHaveBeenCalledWith($rootScope.session.id, book);
    expect(book.inFavorites).toBeTruthy();
  });

  it('should remove from favorites', function () {
    var book = {id:4};
    $rootScope.session = {id: 1};
    var defer = $q.defer();
    BookService.remFromFavorites.and.returnValue(defer.promise);
    defer.resolve();
    ctrl.remFromFavorites(book);
    $scope.$digest();
    expect(BookService.remFromFavorites).toHaveBeenCalledWith($rootScope.session.id, book);
    expect(book.inFavorites).toBeFalsy();
  });

  it('should delete book and reload table', function () {
    var book = {id:4};
    ctrl.tableParams = jasmine.createSpyObj('params', ['reload']);
    var defer = $q.defer();
    BookService.delete.and.returnValue(defer.promise);
    defer.resolve();
    spyOn(ctrl, 'updateFilters');
    ctrl.delete(book);
    $scope.$digest();
    expect(BookService.delete).toHaveBeenCalledWith(book);
    expect(ctrl.tableParams.reload).toHaveBeenCalled();
    expect(ctrl.updateFilters).toHaveBeenCalled();
  });

  it('should call translate function on event', function () {
    spyOn(ctrl, 'translate');
    $rootScope.$broadcast('$translateChangeSuccess');
    expect(ctrl.translate).toHaveBeenCalled();
  });

  it('should call $translate service to get translations', function () {
    ctrl.translate();
    expect($translate).toHaveBeenCalled();
  });

});
