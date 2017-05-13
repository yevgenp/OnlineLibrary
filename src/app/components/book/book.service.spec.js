describe('book service', function () {
  var BookService, $httpBackend, backend;

  module.sharedInjector();
  beforeAll(module('olApp'));
  beforeAll(inject(function (_BookService_, _$httpBackend_, _backend_) {
    BookService = _BookService_;
    $httpBackend = _$httpBackend_;
    backend = _backend_;
  }));

  it('should retrieve books', function () {
    var page = 0;
    var size = 10;
    var sort = {genre: 'desc'};
    var searchObj = {};
    $httpBackend.whenPOST(BookService.getBooksUrl(page, size, sort)).respond(200, 'data');
    var content;
      BookService.getBooks(searchObj, page, size, sort)
      .then( function (response) {
        content = response.data;
      });
    $httpBackend.flush();
    expect(content).toEqual('data');
  });

  it('should get proper url for books', function () {
    var page = 0;
    var size = 10;
    var sort = {genre: 'desc'};
    var url = backend +'api/books/find' + '?page=' + page
      + '&size=' + size + '&sort=' + 'genre,' + sort.genre;
    var result = BookService.getBooksUrl(page, size, sort);
    expect(result).toEqual(url);
  });

  it('should retrieve authors', function () {
    var page = 0;
    var size = 10;
    var searchObj = {};
    $httpBackend.whenPOST(BookService.getAuthorsUrl(page, size)).respond(200, 'data');
    var content;
      BookService.getAuthors(searchObj, page, size)
      .then( function (response) {
        content = response.data;
      });
    $httpBackend.flush();
    expect(content).toEqual('data');
  });

  it('should get proper url for authors', function () {
    var page = 0;
    var size = 10;
    var url = backend + 'api/books/authors' + '?page=' + page + '&size=' + size;
    var result = BookService.getAuthorsUrl(page, size);
    expect(result).toEqual(url);
  });

  it('should retrieve genres', function () {
    var page = 0;
    var size = 10;
    var searchObj = {};
    $httpBackend.whenPOST(BookService.getGenresUrl(page, size)).respond(200, 'data');
    var content;
      BookService.getGenres(searchObj, page, size)
      .then( function (response) {
        content = response.data;
      });
    $httpBackend.flush();
    expect(content).toEqual('data');
  });

  it('should get proper url for genres', function () {
    var page = 0;
    var size = 10;
    var url = backend + 'api/books/genres' + '?page=' + page + '&size=' + size;
    var result = BookService.getGenresUrl(page, size);
    expect(result).toEqual(url);
  });

  it('should upload and call callback', function () {
    callback = jasmine.createSpy('callback');
    $httpBackend.whenPOST(backend + 'api/books')
      .respond(200);
    BookService.upload('book', 'file', callback);
    $httpBackend.flush();
    expect(callback).toHaveBeenCalled();
  });

  it('should pass error response to callback', function () {
    callback = jasmine.createSpy('callback');
    $httpBackend.whenPOST(backend + 'api/books')
      .respond(404);
    BookService.upload('book', 'file', callback);
    $httpBackend.flush();
    expect(callback).toHaveBeenCalled();
  });

  it('should retrieve favorites', function () {
    $httpBackend.whenGET(backend + 'api/users/1/books').respond(200, 'data');
    var content;
    BookService.getFavorites(1)
      .then( function (response) {
        content = response.data;
      });
    $httpBackend.flush();
    expect(content).toEqual('data');
  });

  it('should add to favorites', function () {
    $httpBackend.whenPOST(backend + 'api/users/1/books').respond(200, '');
    var book = {id: 2,_links: {self: {href: 'url'}}};
    var result;
    BookService.addToFavorites(1, book)
      .then( function (response) {
        result = response.data;
      });
    $httpBackend.flush();
    expect(result).toEqual('');
  });

  it('should remove from favorites', function () {
    var book = {id: 2,_links: {self: {href: 'url'}}};
    $httpBackend.whenDELETE(backend + 'api/users/1/books/' + book.id).respond(200, '');
    var result;
    BookService.remFromFavorites(1, book)
      .then( function (response) {
        result = response.data;
      });
    $httpBackend.flush();
    expect(result).toEqual('');
  });

  it('should delete book', function () {
    var book = {id: 2,_links: {self: {href: 'url'}}};
    $httpBackend.whenDELETE(backend + 'api/books/' + book.id).respond(200, '');
    var result;
    BookService.delete(book).then( function (response) {
        result = response.data;
      });
    $httpBackend.flush();
    expect(result).toEqual('');
  });

});
