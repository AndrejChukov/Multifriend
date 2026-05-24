const forms = document.querySelectorAll('.feedbackForm');

forms.forEach(form => {

    const contactMethod = form.querySelector('.contactMethod');

    const contactInfo = form.querySelector('.contactInfo');

    const nameInput = form.querySelector('.nameInput');

    const messageInput = form.querySelector('.messageInput');

    const nameError = form.querySelector('.nameError');

    const contactError = form.querySelector('.contactError');

    const messageError = form.querySelector('.messageError');


    function updatePlaceholder() {

        switch (contactMethod.value) {

            case 'Telegram':
                contactInfo.placeholder = '@username';
                break;

            case 'Email':
                contactInfo.placeholder = 'example@mail.com';
                break;

            case 'WhatsApp':
            case 'Phone':
                contactInfo.placeholder = '+7 999 123 45 67';
                break;

            case 'Max':
                contactInfo.placeholder = '@max_username';
                break;

            default:
                contactInfo.placeholder = 'Введите контакт';
        }
    }

    updatePlaceholder();

    contactMethod.addEventListener('change', updatePlaceholder);


    form.addEventListener('submit', function (e) {

        nameError.textContent = '';
        contactError.textContent = '';
        messageError.textContent = '';

        let isValid = true;


        const nameValue = nameInput.value.trim();

        const nameRegex = /^[А-Яа-яA-Za-zЁё\s-]+$/;

        if (!nameRegex.test(nameValue)) {

            isValid = false;

            nameError.textContent =
                'Имя должно содержать только буквы';
        }


        const method = contactMethod.value;

        const value = contactInfo.value.trim();


        if (method === 'Telegram') {

            const telegramRegex = /^@?[a-zA-Z0-9_]{5,32}$/;

            if (!telegramRegex.test(value)) {

                isValid = false;

                contactError.textContent =
                    'Введите корректный Telegram username';
            }
        }


        if (method === 'Email') {

            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

            if (!emailRegex.test(value)) {

                isValid = false;

                contactError.textContent =
                    'Введите корректный Email';
            }
        }


        if (method === 'WhatsApp' || method === 'Phone') {

            const digitsOnly = value.replace(/\D/g, '');

            const isRussianPhone =
                digitsOnly.length === 11 &&
                (digitsOnly.startsWith('7') || digitsOnly.startsWith('8'));

            if (!isRussianPhone) {

                isValid = false;

                contactError.textContent =
                    'Введите корректный российский номер';
            }
        }

        if (method === 'Max') {

            const maxRegex = /^@?[a-zA-Z0-9_]{3,32}$/;

            if (!maxRegex.test(value)) {

                isValid = false;

                contactError.textContent =
                    'Введите корректный username Max';
            }
        }


        const messageValue = messageInput.value.trim();

        if (messageValue.length < 10) {

            isValid = false;

            messageError.textContent =
                'Сообщение должно содержать минимум 10 символов';
        }


        if (!isValid) {

            e.preventDefault();
        }
    });
});