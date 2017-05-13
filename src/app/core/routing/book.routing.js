angular
  .module('olApp')
  .config(BookRouting);

function BookRouting($stateProvider) {
  $stateProvider

    .state('main.books', {
      url: 'books',
      templateUrl: 'app/components/book/list/list.book.html',
      controller: 'ListBookController',
      controllerAs: 'ctrl',
      role: 'ANY'
    })

  ;

}

