describe('navbar directive', function () {
  var element, $scope, $rootScope, LoginService, LocaleService;

  module.sharedInjector();
  beforeAll(module('olApp'));
  beforeAll(module(function($provide) {
    $provide.decorator('LoginService', function ($delegate) {
      $delegate.logout = jasmine.createSpy();
      return $delegate;
    });
    $provide.decorator('LocaleService', function ($delegate) {
      $delegate.initLanguages = jasmine.createSpy();
      $delegate.activateLanguage = jasmine.createSpy();
      return $delegate;
    });
  }));
  beforeAll(inject(function (_$rootScope_, $compile, _LoginService_, _LocaleService_) {
    $rootScope = _$rootScope_;
    LoginService = _LoginService_;
    LocaleService = _LocaleService_;
    element = angular.element('<ol-navbar></ol-navbar>');
    $compile(element)($rootScope.$new());
    $rootScope.$digest();
    $scope = element.isolateScope();
  }));


  it('should compile', function () {
    expect(element.html()).not.toEqual(null);
    expect($scope).toBeDefined();
  });

  it('should init languages on start', function () {
    expect(LocaleService.initLanguages).toHaveBeenCalled();
  });

  it('refreshes $scope.session on $rootScope.session change', function () {
    $scope.session = {authenticated: false};
    $rootScope.session = {authenticated: true};
    $rootScope.$apply();
    expect($scope.session).toEqual({authenticated: true});
  });

  it('calls LoginService to logout', function () {
    $scope.logout();
    expect(LoginService.logout).toHaveBeenCalled();
  });

  it('should activate chosen language', function () {
    $scope.activateLanguage('uk');
    expect(LocaleService.activateLanguage).toHaveBeenCalledWith('uk');
  });
});
