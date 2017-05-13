describe('registration controller', function() {
  var ctrl, $location, $scope, $rootScope,
    $q, defer, response, $httpBackend, backend, $timeout, $translate;

  module.sharedInjector();
  beforeAll(module('olApp'));
  beforeAll(inject(function(_$rootScope_, _$controller_, _$httpBackend_,
                            _backend_, _$timeout_, _$q_) {
    $rootScope = _$rootScope_;
    $scope = _$rootScope_.$new();
    $q = _$q_;
    defer = $q.defer();
    $httpBackend = _$httpBackend_;
    backend = _backend_;
    $timeout = _$timeout_;
    $translate = jasmine.createSpy('$translate').and.returnValue(defer.promise);
    $location = jasmine.createSpyObj('$location', ['path']);
    ctrl = _$controller_('RegistrController', {
      $scope: $scope,
      $location: $location,
      $translate: $translate
    });
  }));

  it('should have initial properties defined', function () {
    expect(ctrl).toBeDefined();
    expect(ctrl.user).toBeDefined();
    expect(ctrl.error).toBeFalsy();
    expect(ctrl.success).toBeFalsy();
  });

  it('should not send http request when form is invalid', function () {
    $scope.form = {
      $valid : false
    };
    ctrl.submit();
    $httpBackend.verifyNoOutstandingRequest();
  });

  it('should post http request when form is valid', function () {
    $scope.form = {
      $valid : true
    };
    $httpBackend.whenPOST(backend + 'api/users/register').respond(200);
    ctrl.submit();
    $httpBackend.flush();
    $httpBackend.verifyNoOutstandingRequest();
  });

  it('should redirect to /login when registered successfully', function () {
    ctrl.onSuccess();
    $timeout.flush();
    expect($location.path).toHaveBeenCalledWith('/login');
    expect(ctrl.error).toBeFalsy();
    expect(ctrl.success).toBeTruthy();
  });

  it('should set error on error response', function () {
    ctrl.onError({data:[]});
    expect(ctrl.error).toBeTruthy();
  });

  it('should call translate() on event', function () {
    spyOn(ctrl, 'translate');
    $rootScope.$broadcast('$translateChangeSuccess');
    $rootScope.$digest();
    expect(ctrl.translate).toHaveBeenCalled();
  });

  it('should call $translate for translation', function () {
    ctrl.translate();
    $scope.$digest();
    expect($translate).toHaveBeenCalled();
  });

});
