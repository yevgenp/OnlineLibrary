angular
  .module('olApp')
  .controller('LoginController', LoginController);

function LoginController(LoginService, $location) {
  var ctrl = this;
  ctrl.credentials = {};

  ctrl.login = function () {
    LoginService.authenticate(ctrl.credentials, ctrl.onLoginResult)
  };

  ctrl.onLoginResult = function (authenticated) {
    $location.path(authenticated ? "/books" : "/login");
    ctrl.error = !authenticated;
  };
}
