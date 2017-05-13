angular
  .module('olApp')
  .directive("valueMatch", function() {
    return {
      require: "ngModel",
      scope: {
        otherModelValue: "=valueMatch"
      },
      link: function(scope, element, attributes, ngModel) {
        ngModel.$validators.valueMatch = function(modelValue) {
          return modelValue == scope.otherModelValue;
        };
        scope.$watch("otherModelValue", function() {
          ngModel.$validate();
        });
      }
    };
  });
