angular
  .module('olApp')
  .factory('LoginService', LoginService);

function LoginService($http, $rootScope, $location, backend) {
  var service = {};
  $rootScope.session = {};

  service.getHeaders = function (credentials) {
    return credentials ? {
      authorization: "Basic " + btoa(encodeURI(credentials.username + ":" + credentials.password))
    } : {};
  };

  function setRoles(response) {
    $rootScope.session.roles = [];
    if (response.data.authorities && response.data.authorities.length > 0) {
      response.data.authorities.forEach(function (authority) {
        $rootScope.session.roles.push(authority.authority);
      });
    }
  }

  function setUserRoleFunction() {
    $rootScope.session.hasUserRole = function (userRole) {
      var answer = false;
      this.roles.forEach(function (role) {
        if (role === userRole) {
          answer = true;
        }
      });
      return answer;
    };
  }

  function setUserInfo(data) {
    $rootScope.session.username = data.name;
    if (data && data.principal) {
      $rootScope.session.id = data.principal.id;
      $rootScope.session.firstName = data.principal.firstName;
      $rootScope.session.lastName = data.principal.lastName;
    }
  }

  service.authenticate = function (credentials, callback) {
    $http.get(backend + 'api/authentication/login', {
      headers: service.getHeaders(credentials)})
      .then(
        function (response) {
          $rootScope.session = {};
          $rootScope.session.authenticated = !!response.data.name;
          setUserInfo(response.data);
          setRoles(response);
          setUserRoleFunction();
          callback && callback($rootScope.session.authenticated);
        },
        function () {
          $rootScope.session.authenticated = false;
          callback && callback(false);
        });
  };

  service.logout = function () {
    $http.get(backend + 'api/authentication/logout', {}).finally(function () {
      $rootScope.session = {};
      $location.path("/login");
    });
  };

  return service;
}
