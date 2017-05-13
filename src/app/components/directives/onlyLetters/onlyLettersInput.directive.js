'use strict';

angular
  .module('olApp')
  .directive('onlyLettersInput', onlyLettersInput);

  function onlyLettersInput() {
    return {
      require: 'ngModel',
      link: function(scope, element, attr, ngModel) {
        function fromUser(text) {
          var regexToReplace = /[\d\\|~!@#$%\^&*()\-_+=?\/,.`"':;<>]/g;
          var transformedInput = text.replace(regexToReplace, '');
          if (transformedInput !== text) {
            ngModel.$setViewValue(transformedInput);
            ngModel.$render();
          }
          return transformedInput;
        }
        ngModel.$parsers.push(fromUser);
      }
    };
  }
