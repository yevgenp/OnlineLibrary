angular
  .module('olApp')
  .directive('olNavbar', olNavbar);

function olNavbar() {
  return {
    restrict: 'E',
    templateUrl: 'app/components/navbar/navbar.html',
    scope: {},
    controller: NavbarController,
    controllerAs: 'ctrl',
    bindToController: true
  };

  function NavbarController($scope, $rootScope,
                            LoginService, LocaleService) {

    LocaleService.initLanguages();

    $scope.languages = LocaleService.languages;

    $rootScope.$watch('session.authenticated', function () {
      $scope.session = $rootScope.session;
    });

    $scope.logout = function () {
      LoginService.logout();
    };

    $scope.activateLanguage = function (language) {
      LocaleService.activateLanguage(language);
    };

  }
}

