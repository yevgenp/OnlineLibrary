angular
  .module('olApp')
  .config(MainRouting);

function MainRouting($stateProvider, $urlRouterProvider) {
  $stateProvider

    .state('main', {
      url: '/',
      templateUrl: 'app/components/main/main.html',
      controller: 'MainController',
      controllerAs: 'main'
    })

    .state('main.welcome', {
      url: 'welcome',
      templateUrl: 'app/components/guest/welcome/welcome.html'
    })

    .state('main.login', {
      url: 'login',
      templateUrl: 'app/components/guest/login/login.html',
      controller: 'LoginController',
      controllerAs: 'ctrl'
    })

    .state('main.home', {
      url: 'home',
      templateUrl: 'app/components/user/home/home.html',
      role: 'USER'
    })

    .state('main.registration', {
      url: 'registration',
      templateUrl: 'app/components/guest/registration/registration.html',
      controller: 'RegistrController',
      controllerAs: 'ctrl',
      role: 'ANY'
    })
  ;

  $urlRouterProvider.otherwise('/');
}

