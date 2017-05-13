const conf = require('./gulp.conf');
const listFiles = require('./karma-files.conf');

module.exports = function (config) {
  const configuration = {
    basePath: '../',
    singleRun: true,
    autoWatch: false,
    logLevel: 'WARN',
    browsers: [
      'PhantomJS'
    ],
    client: {
      captureConsole: true
    },
    frameworks: [
      'phantomjs-shim',
      'jasmine',
      'angular-filesort'
    ],
    files: listFiles(),
    reporters: ['progress', 'coverage'],
    preprocessors: {
      [conf.path.src('**/*.html')]: [
        'ng-html2js'
      ],
      [conf.path.src('**/!(*.spec).js')]: ['coverage']
    },
    coverageReporter: {
      dir: 'coverage',
      reporters: [
        {type: 'html', subdir: 'html'},
        {type: 'cobertura', subdir: 'cobertura'}
      ]
    },
    ngHtml2JsPreprocessor: {
      stripPrefix: `${conf.paths.src}/`,
      moduleName: 'olApp'
    },
    junitReporter: {
      outputDir : 'coverage/junit/'
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
