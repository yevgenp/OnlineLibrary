describe('onlyLettersInput directive', function() {
  var $scope, element;

  beforeEach(module('olApp'));

  beforeEach(inject(function($compile, $rootScope) {
    $scope = $rootScope.$new();
    element = angular.element(
      '<input only-letters-input ng-model="onlyLettersInput"/>'
    );
    $compile(element)($scope);
    $scope.$digest();
  }));

  it('should leave only letters in input', function () {
    element.val('A2 sD#5').triggerHandler('change');
    $scope.$digest();
    expect(element.val()).toEqual('A sD');
  });
});
