angular
  .module('olApp')
  .factory('BookService', BookService);

function BookService($http, backend) {
  var service = {};

  service.getBooks = function(searchObj, page, size, sort) {
    var url = service.getBooksUrl(page, size, sort);
    return $http.post(url, searchObj);
  };

  service.getBooksUrl = function (page, size, sort) {
    var sorting = '';
    for (var prop in sort)
      sorting += '&sort=' + prop + ',' + sort[prop];
    return backend +'api/books/find' + '?page=' + page + '&size=' + size + sorting;
  };

  service.getAuthors = function(searchObj, page, size) {
    var url = service.getAuthorsUrl(page, size);
    return $http.post(url, searchObj);
  };

  service.getAuthorsUrl = function (page, size) {
    return backend + 'api/books/authors' + '?page=' + page + '&size=' + size;
  };

  service.getGenres = function(searchObj, page, size) {
    var url = service.getGenresUrl(page, size);
    return $http.post(url, searchObj);
  };

  service.getGenresUrl = function (page, size) {
    return backend + 'api/books/genres' + '?page=' + page + '&size=' + size;
  };

  service.upload = function (book, file, callback) {
    var formData = new FormData();
    formData.append('book',  new Blob([angular.toJson(book)], {type: "application/json"}));
    formData.append('file', file);

    $http.post(backend + 'api/books',
      formData, {
      transformRequest: angular.identity,
      headers: {'Content-Type': undefined }
    }).then(
      function onSuccess(response) {
        callback && callback(response);
      },
      function onError(response) {
        callback && callback(response);
      });
  };

  service.getFavorites = function (userId) {
    var url = backend + 'api/users/' + userId + '/books';
    return $http.get(url);
  };

  service.addToFavorites = function (userId, book) {
    var url = backend + 'api/users/' + userId + '/books';
    return $http.post(url, book._links.self.href,
      {headers: {'Content-Type': 'text/uri-list' }});
  };

  service.remFromFavorites = function (userId, book) {
    var url = backend + 'api/users/' + userId + '/books/' + book.id;
    return $http.delete(url);
  };

  service.delete = function (book) {
    var url = backend + 'api/books/' + book.id;
    return $http.delete(url);
  };

  return service;
}
