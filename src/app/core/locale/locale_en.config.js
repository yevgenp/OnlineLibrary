angular
  .module('olApp')
  .config(configTranslateProviderEN);

function configTranslateProviderEN($translateProvider) {
  var translation = {

    book: {
      addBookTitle: "Add new book",
      addSuccess: "Book has been added to library",
      action: "Action",
      author: "Author",
      description: "Description",
      fetchError: "Could not fetch books list !",
      file: "File",
      error: "Invalid field(s) content",
      fileRequired: "Book file is required",
      genre: "Genre",
      list: {
        title: "Library books"
      },
      title: "Title"
    },

    button: {
      add: "Add",
      close: "Close",
      download: "Download",
      login: "Login",
      new: "New",
      register: "Register",
      upload: "Upload"
    },

    common: {
      failure: "Failure",
      fieldRequired: "This field is required",
      filters: "Filters",
      isInvalid: "is invalid",
      search: "Search by title and description",
      welcome: "Welcome to online library!",
      unknownError: "Unknown error. Try again later"
    },

    login: {
      error: "There was a problem logging in. Please try again."
    },

    navbar: {
      administration: "Administration",
      books: "Books",
      createAccount: "Create account",
      login: "Log in",
      logout: "Log out",
      profile: "Profile",
      register: "Register",
      title: "Online Library",
    },

    registration: {
      successText: "You have successfully registered. Use supplied credentials to login"
    },

    user: {
      email: "Email",
      emailInvalid: "Email is invalid",
      firstName: "First name",
      isAlreadyRegistered: "Username is already registered. Choose another one",
      lastName: "Last name",
      password: "Password",
      passwordConfirm: "Password confirmation",
      passwordConfirmInvalid: "Passwords don't match",
      passwordInvalid: "Password can't be empty",
      username: "Login",
      usernameAsLogin: "Username (will be used as login)",
      usernameInvalid: "Username may consist only of Latin letters and numbers",

    }

  };

  $translateProvider
    .useSanitizeValueStrategy('escape')
    .useLocalStorage()
    .translations('en', translation)
    .preferredLanguage('en')
    .fallbackLanguage('en');
}
