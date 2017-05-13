describe('login service', function () {
  var LoginService, $httpBackend, $rootScope, $location, backend;
  var credentials = {username: '12345678910', password: 'password'};

  beforeEach(module('olApp'));
  beforeEach(inject(function (_LoginService_, _$httpBackend_, _$rootScope_, _$location_, _backend_) {
    LoginService = _LoginService_;
    $httpBackend = _$httpBackend_;
    $rootScope = _$rootScope_;
    $location = _$location_;
    backend = _backend_;
    $rootScope.session = {authenticated: false};
    spyOn($location, 'path');
  }));

  it('should be defined', function () {
    expect(LoginService).toBeDefined();
  });

  it('should have authenticate, logout and getHeaders functions', function () {
    expect(LoginService.getHeaders).toBeDefined();
    expect(LoginService.authenticate).toBeDefined();
    expect(LoginService.logout).toBeDefined();
  });

  it('should return authorization header if credentials are provided', function () {
    var headers = LoginService.getHeaders(credentials);
    expect(headers).toEqual({authorization: 'Basic MTIzNDU2Nzg5MTA6cGFzc3dvcmQ='});
  });

  it('should return empty authorization header if credentials are undefined', function () {
    credentials = undefined;
    var headers = LoginService.getHeaders(credentials);
    expect(headers).toEqual({});
  });

  it('should reset session and redirect on logout', function () {
    respondOkWhenLogout();
    $rootScope.session = {authenticated: true};
    LoginService.logout();
    $httpBackend.flush();
    expect($rootScope.session).toEqual({});
    expect($location.path).toHaveBeenCalled();
  });

  it('should set authenticated status as response data name on login', function () {
    var callback = jasmine.createSpy('spy');
    respondOkWhenLogin(credentials);
    LoginService.authenticate(credentials, callback);
    $httpBackend.flush();
    expect($rootScope.session.authenticated).toBeTruthy();
    expect($rootScope.session.roles).toEqual(['USER']);
    expect($rootScope.session.hasUserRole).toBeDefined();
    expect($rootScope.session.hasUserRole('USER')).toBeTruthy();
    expect($rootScope.session.hasUserRole('ADMINISTARTOR')).toBeFalsy();
    expect(callback).toHaveBeenCalled();
  });

  it('should set authentication status as false on error during log in', function () {
    var callback = jasmine.createSpy('spy');
    respondUnauthorizedWhenLogin(credentials);
    LoginService.authenticate(credentials, callback);
    $httpBackend.flush();
    expect($rootScope.session.authenticated).toBeFalsy();
    expect(callback).toHaveBeenCalled();
  });

  function respondOkWhenLogin(credentials) {
    var headers = {headers: LoginService.getHeaders(credentials)};
    $httpBackend.when('GET', backend + 'api/authentication/login', headers).respond(200, {name: 'bobby@gmail.com', authorities: [{authority: 'USER'}]});
  }

  function respondUnauthorizedWhenLogin(credentials) {
    var headers = {headers: LoginService.getHeaders(credentials)};
    $httpBackend.when('GET', backend + 'api/authentication/login', headers).respond(401, {});
  }

  function respondOkWhenLogout() {
    $httpBackend.when('GET', backend + 'api/authentication/logout').respond(200, {data: '...'});
  }

});
