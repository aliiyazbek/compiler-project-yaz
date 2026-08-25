// Client-side behaviour for the generated site.
//
// This file is a support asset: the compiler copies it into output/ verbatim
// and never parses or rewrites it, exactly like style.css and the images.

(function () {
    'use strict';

    // Ask before a delete actually goes through — the confirmation page posts
    // straight to /confirm-delete/{id}, which cannot be undone.
    function confirmDeletes() {
        var forms = document.querySelectorAll('form[action^="/confirm-delete/"]');
        Array.prototype.forEach.call(forms, function (form) {
            form.addEventListener('submit', function (event) {
                if (!window.confirm('Delete this member? This cannot be undone.')) {
                    event.preventDefault();
                }
            });
        });
    }

    // Block submitting the add/edit forms with an empty name, so a blank row
    // never reaches the data store.
    function requireName() {
        var forms = document.querySelectorAll('form[method="POST"]');
        Array.prototype.forEach.call(forms, function (form) {
            var nameField = form.querySelector('input[name="name"]');
            if (!nameField) {
                return;
            }
            form.addEventListener('submit', function (event) {
                if (nameField.value.trim() === '') {
                    event.preventDefault();
                    nameField.focus();
                    window.alert('Please enter a member name.');
                }
            });
        });
    }

    document.addEventListener('DOMContentLoaded', function () {
        confirmDeletes();
        requireName();
    });
}());
