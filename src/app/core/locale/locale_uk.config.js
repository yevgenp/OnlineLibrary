angular
  .module('olApp')
  .config(configTranslateProviderPL);

function configTranslateProviderPL($translateProvider) {
  var translation = {

    book: {
      addBookTitle: "Додавання нової книги",
      addSuccess: "Книга була додана до бібліотеки",
      action: "Дія",
      author: "Автор",
      description: "Опис",
      fetchError: "Не можливо отримати список книг !",
      file: "Файл",
      fileError: "Файл пошкоджений",
      fileRequired: "Файл книги є обов'язковим",
      genre: "Жанр",
      list: {
        title: "Бібліотечний фонд"
      },
      title: "Назва"
    },

    button: {
      add: "Додати",
      close: "Закрити",
      download: "Завантажити",
      login: "Ввійти",
      new: "Новий",
      register: "Зареєструватися",
      upload: "Зберегти"
    },

    common: {
      failure: "Помилка",
      fieldRequired: "Це поле є обов'язковим",
      filters: "Фільтри",
      isInvalid: "є не правильним",
      search: "Пошук за назвою та описом",
      welcome: "Вітаємо в Онлайн Бібліотеці !",
      unknownError: "Невідома помилка. Спробуйте пізніше"
    },

    login: {
      error: "Помилка при спробі входу. Спробуйте ще раз"
    },

    navbar: {
      administration: "Адміністрування",
      books: "Книги",
      createAccount: "Створити обліковий запис",
      login: "Ввійти",
      logout: "Вийти",
      profile: "Профіль",
      register: "Зареєструватися",
      title: "Онлайн Бібліотека",
    },

    registration: {
      successText: "Ви успішно зареєструвалися. Використайте введені облікові дані для входу"
    },

    user: {
      email: "Ел. пошта",
      emailInvalid: "Ел. пошта не коректна",
      firstName: "Ім'я",
      isAlreadyRegistered: "Користувач з даним іменем вже зареєстрований. Оберіть інше",
      lastName: "Прізвище",
      password: "Пароль",
      passwordConfirm: "Підтвердження паролю",
      passwordConfirmInvalid: "Паролі не співпадають",
      passwordInvalid: "Пароль не може бути пустим",
      username: "Ім'я користувача",
      usernameAsLogin: "Ім'я користувача (використовується для входу)",
      usernameInvalid: "Ім'я користувача може містити лише латин. літери та цифри",
    }

  };


  $translateProvider
    .translations('uk', translation);
}
