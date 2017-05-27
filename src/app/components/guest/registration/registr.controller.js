'use strict';

angular
  .module('olApp')
  .controller('RegistrController', RegistrController);

function RegistrController($scope, $rootScope, $location,
                            $http, backend, $timeout, $translate) {
  var ctrl = this;
  ctrl.user = {};
  ctrl.translations = {};
  ctrl.error = false;
  ctrl.success = false;

  ctrl.submit =  function() {
    if ($scope.form.$valid) {
      var userToSend = angular.copy(ctrl.user);
      userToSend.password = btoa(encodeURI(userToSend.password));
      userToSend.passwordConfirm = null;
      $http.post(backend + 'api/users/register', userToSend)
        .then(ctrl.onSuccess, ctrl.onError);
    }
  };

  ctrl.onSuccess = function(response) {
    ctrl.error = false;
    ctrl.success = true;
    $timeout(function () {
      $location.path("/login");
    }, 3000);
  };

  function capitalize(s) {
    return s[0].toUpperCase() + s.slice(1);
  }

  ctrl.onError = function (response) {
        ctrl.errorText = [];
        response.data.forEach(function (item) {
          if (item.code === 'user.username.alreadyUsed')
            ctrl.errorText.push(ctrl.translations['isAlreadyRegistered']);
          else
            ctrl.errorText.push(capitalize(item.field) + ' ' + ctrl.translations['isInvalid']);
        });
        ctrl.error = true;
  };

  $rootScope.$on('$translateChangeSuccess', function() {ctrl.translate()});

  ctrl.translate = function () {
    $translate(['user.isAlreadyRegistered', 'common.isInvalid'])
      .then(function (translations) {
      ctrl.translations['isAlreadyRegistered'] = translations['user.isAlreadyRegistered'];
      ctrl.translations['isInvalid'] = translations['common.isInvalid'];
    });
  };

  ctrl.translate();
}
