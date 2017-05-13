describe('account service', function () {
  var  service, $rootScope, $translate, $httpBackend, backend;

  beforeEach(module('olApp'));

  beforeEach(inject(function (_$rootScope_, _LocaleService_,
                              _$httpBackend_, _backend_, _$translate_) {
    $rootScope = _$rootScope_;
    service = _LocaleService_;
    $httpBackend = _$httpBackend_;
    backend = _backend_;
    $translate = _$translate_;
  }));

  it('should determine and activate current language', function () {
    spyOn($translate, 'use');
    spyOn(service, 'activateLanguage');
    service.initLanguages();
    expect($translate.use).toHaveBeenCalledWith();
    expect(service.activateLanguage).toHaveBeenCalled();
  });

  it('should activate chosen language', function () {
    spyOn($translate, 'use');
    var en = {id: "en", title: "English", icon: "gb"};
    var uk = {id: "uk", title: "Ukrainian", icon: "uk"};
    service.languages = [en, uk];
    $httpBackend.whenGET(backend + 'api/authentication/login?locale=' + uk.id).respond(200);
    service.activateLanguage(uk);
    expect($translate.use).toHaveBeenCalledWith(uk.id);
    expect(service.languages).toEqual([uk, en]);
    $httpBackend.flush();
    $httpBackend.verifyNoOutstandingRequest();
  });

});
