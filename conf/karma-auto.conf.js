const conf = require('./gulp.conf');
const listFiles = require('./karma-files.conf');

module.exports = function (config) {
  const configuration = {
    basePath: '../',
    singleRun: false,
    autoWatch: true,
    logLevel: 'ERROR',
    client: {
      captureConsole: false
    },
    junitReporter: {
      outputDir: 'test-reports'
    },
    browsers: [
      'PhantomJS'
    ],
    frameworks: [
      'phantomjs-shim',
      'jasmine',
      'angular-filesort'
    ],
    files: listFiles(),
    preprocessors: {
      [conf.path.src('**/*.html')]: [
        'ng-html2js'
      ]
    },
    ngHtml2JsPreprocessor: {
      stripPrefix: `${conf.paths.src}/`,
      moduleName: 'olApp'
    },
    angularFilesort: {
      whitelist: [
        conf.path.tmp('**/!(*.html|*.spec|*.mock).js')
      ]
    },
    plugins: [
      require('karma-jasmine'),
      require('karma-junit-reporter'),
      require('karma-coverage'),
      require('karma-phantomjs-launcher'),
      require('karma-phantomjs-shim'),
      require('karma-ng-html2js-preprocessor'),
      require('karma-angular-filesort')
    ]
  };

  config.set(configuration);
};