angular
  .module('olApp')
  .controller('MainController', MainController);

function MainController($rootScope, $state, $timeout,
                        LoginService, NavigationHandler) {
  var ctrl = this;
  ctrl.viewProperties = {loaded: false};


  ctrl.onAuthenticationCompleted = function () {
    var session = $rootScope.session;
    var nextLocation = $state.current;
    if (nextLocation.role != 'ANY') {
      NavigationHandler.handleNavigationToRoot(nextLocation, session);
      NavigationHandler.handleNavigationToProtectedPages(nextLocation, session);
      NavigationHandler.handleNavigationToGuestPagesWhenLoggedIn(nextLocation, session);
    }
  };

  ctrl.showContentWhenLoaded = function () {
    $timeout(function () {ctrl.viewProperties.loaded = true;}, 150);
  };

  LoginService.authenticate(null, ctrl.onAuthenticationCompleted);
  ctrl.showContentWhenLoaded();

}

