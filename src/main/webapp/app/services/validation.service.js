(function () {
    'use strict';

    angular
        .module('tourologistApp.services')
        .service('ValidationService', ['UtilService', ValidationService]);

    function ValidationService(util) {
        var service = this;

        //////////////////////////////////////////////////

        service.presence = function (subject, path, message) {
            var value = get(subject, path);
            if (value === null || value === undefined || value === "") {
                set(subject.errors, path, message || defaultMessage('presence', path));
            }
        };

        service.minLength = function (subject, path, length, message) {
            var value = get(subject, path);
            if (value && value.length < length) {
                set(subject.errors, path, message || defaultMessage('minLength', path, length));
            }
        };

        service.length = function (subject, path, length, message) {
            var value = get(subject, path);
            if (value && value.trim().length !== length) {
                set(subject.errors, path, message || defaultMessage('length', path, length));
            }
        };

        service.maxLength = function (subject, path, length, message) {
            var value = get(subject, path);
            if (value && value.length > length) {
                set(subject.errors, path, message || defaultMessage('maxLength', path, length));
            }
        };

        service.nonEmptyArray = function (subject, path, message) {
            var value = get(subject, path);
            if (value && value.length == 0) {
                set(subject.errors, path, message || defaultMessage('nonEmptyArray', path));
            }
        };

        service.digits = function (subject, path, message) {
            var value = get(subject, path);
            if (value && !value.match(/^[0-9]+$/)) {
                set(subject.errors, path, message || defaultMessage('digits', path));
            }
        };

        service.presenceAtLeastOneOf = function (subject, pathes, message) {
            if (gets(subject, pathes).length == 0) {
                _.each(pathes, function (path) {
                    set(subject.errors, path, message || defaultMessage('presenceAtLeastOneOf', path, attributes(pathes).join(', ')));
                });
            }
        };

        service.presenceNotMoreThanOne = function (subject, pathes, message) {
            if (gets(subject, pathes).length > 1) {
                _.each(pathes, function (path) {
                    set(subject.errors, path, message || defaultMessage('presenceNotMoreThenOne', path, attributes(pathes).join(', ')));
                });
            }
        };

        service.email = function (subject, path, message) {
            var value = get(subject, path);
            if (value && !util.isEmail(value)) {
                set(subject.errors, path, message || defaultMessage('email', path));
            }
        };

        service.url = function (subject, path, message) {
            var value = get(subject, path);
            if (value && !util.isURL(value)) {
                set(subject.errors, path, message || defaultMessage('url', path));
            }
        };

        service.truth = function (subject, path, message) {
            if (get(subject, path) !== true) {
                set(subject.errors, path, message || defaultMessage('truth', path));
            }
        };

        service.date = function (subject, path, format, message) {
            var value = get(subject, path);
            if (value && !util.isDate(value.toString())) {
                set(subject.errors, path, message || defaultMessage('date', path));
            }
        };

        service.format = function (subject, path, format, message) {
            var value = get(subject, path);
            if (value && !value.toString().match(format)) {
                set(subject.errors, path, message || defaultMessage('format', path));
            }
        };

        service.numberGreaterOrEqualThan = function (subject, path, otherNumber, message) {
            if (!get(subject.errors, path) && (Number(get(subject, path)) < Number(otherNumber))) {
                set(subject.errors, path, message || defaultMessage('numberGreaterOrEqualThan', path, otherNumber));
            }
        };

        service.numberLessOrEqualThan = function (subject, path, otherNumber, message) {
            if (!get(subject.errors, path) && (Number(get(subject, path)) > Number(otherNumber))) {
                set(subject.errors, path, message || defaultMessage('numberLessOrEqualThan', path, otherNumber));
            }
        };

        service.positiveInteger = function (subject, path, message) {
            var value = get(subject, path);
            if (value && !value.toString().match(/^0*[1-9][0-9]*$/)) {
                set(subject.errors, path, message || defaultMessage('positiveInteger', path));
            }
        };

        service.positiveIntegerOrZero = function (subject, path, message) {
            var value = get(subject, path);
            if (value && !value.toString().match(/^((0*[1-9][0-9]*)|0)$/)) {
                set(subject.errors, path, message || defaultMessage('positiveIntegerOrZero', path));
            }
        };

        service.positiveNumber = function (subject, path, message) {
            var value = get(subject, path);
            if (value && !value.toString().match(/^(((0*[1-9][0-9]*)(\.[0-9]+)?)|((0+)?\.0*[1-9][0-9]*))$/)) {
                set(subject.errors, path, message || defaultMessage('positiveNumber', path));
            }
        };

        service.positiveNumberOrZero = function (subject, path, message) {
            var value = get(subject, path);
            if (value && !value.toString().match(/^(((0*[1-9][0-9]*)|0)(\.[0-9]+)?|(\.[0-9]+))$/)) {
                set(subject.errors, path, message || defaultMessage('positiveNumberOrZero', path));
            }
        };

        service.number = function (subject, path, message) {
            var value = get(subject, path);
            if (value && !util.isNumber(value)) {
                set(subject.errors, path, message || defaultMessage('number', path));
            }
        };

        service.percent = function (subject, path, message) {
            var value = get(subject, path);
            if (value) {
                var number = _.toNumber(value);
                if (isNaN(number) || number < 0 || number > 100) {
                    set(subject.errors, path, message || defaultMessage('percent', path));
                }
            }
        };

        service.hex = function (subject, path, message) {
            var value = get(subject, path);
            if (value && !/(^#[0-9A-F]{6}$)|(^#[0-9A-F]{3}$)/i.test(value)) {
                set(subject.errors, path, message || defaultMessage('hex', path));
            }
        };

        //////////////////////////////////////////////////

        function get(subject, path) {
            return _.get(subject, subjectPath(path));
        };

        function set(subject, path, value) {
            return _.set(subject, subjectPath(path), value);
        };

        function subjectPath(path) {
            return path.split('.').slice(1).join('.');
        };

        function gets(subject, pathes) {
            return _.filter(pathes, function (path) {
                return util.isPresent(get(subject, path));
            });
        };

        function attributes(pathes) {
            return _.map(pathes, function (path) {
                return attribute(path);
            });
        };

        function attribute(path) {
            return path.split('.').slice(-1)[0];
        };

        function defaultMessage(validator, path, value) {
            return {
                'presence': _.template('Enter <%= attribute %>'),
                'nonEmptyArray': _.template('Enter <%= attribute %>'),
                'presenceAtLeastOneOf': _.template('Enter at least one of: <%= value %>'),
                'presenceNotMoreThenOne': _.template('Only one value can be entered: <%= value %>'),
                'email': _.template('Entered value is not correct email'),
                'url': _.template('Entered value is not correct URL'),
                'number': _.template('Entered value is not correct number'),
                'format': _.template('Entered value has incorrect format'),
                'date': _.template('Entered value is not correct date'),
                'positiveInteger': _.template('Entered value is not correct positive integer'),
                'positiveIntegerOrZero': _.template('Entered value is not correct positive integer or zero'),
                'positiveNumber': _.template('Entered value is not correct positive number'),
                'positiveNumberOrZero': _.template('Entered value is not correct positive number or zero'),
                'percent': _.template('Entered value is not correct percent'),
                'hex': _.template('Entered valid color. It should be hex code started with #'),
                'numberLessOrEqualThan': _.template('Entered number should be less or equal than <%= value %>'),
                'numberGreaterOrEqualThan': _.template('Entered number should be greater or equal than <%= value %>'),
                'minLength': _.template('Minimum length of <%= attribute %> is <%= value %>'),
                'maxLength': _.template('Maximum length of <%= attribute %> is <%= value %>'),
                'length': _.template('The Length of <%= attribute %> should be <%= value %>'),
                'digits': _.template('Entered value contains non digits'),
            }[validator]({attribute: _.startCase(attribute(path)), value: value || ''});
        };
    }

})();
