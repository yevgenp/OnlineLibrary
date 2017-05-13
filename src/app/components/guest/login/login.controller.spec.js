describe('login controller', function () {
  var ctrl, $location, LoginService;
  var credentials = {username: '12345678910', password: 'password'};


  module.sharedInjector();
  beforeAll(module('olApp'));
  beforeAll(inject(function (_$controller_, _$location_, _LoginService_) {
    ctrl = _$controller_('LoginController');
    $location = _$location_;
    LoginService = _LoginService_;
    spyOn(LoginService, 'authenticate');
    spyOn($location, 'path');
  }));

  it('should have initial properties', function () {
    expect(ctrl.credentials).toBeDefined();
  });

  it('should call LoginService authenticate method logging in', function () {
    ctrl.login(credentials);
    expect(LoginService.authenticate).toHaveBeenCalled();
  });

  it(' should redirect to another page if authenticated successfully', function () {
    ctrl.onLoginResult(true);
    expect($location.path).toHaveBeenCalledWith('/books');
    expect(ctrl.error).toEqual(false);
  });

  it('should redirect to /login otherwise', function () {
    ctrl.onLoginResult(false);
    expect($location.path).toHaveBeenCalledWith('/login');
    expect(ctrl.error).toEqual(true);
  });

});

