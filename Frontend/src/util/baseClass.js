import Toastify from "toastify-js";

export default class BaseClass {
    /**
     * Binds all of the methods to "this" object. These methods will now have the state of the instance object.
     * @param methods The name of each method to bind.
     * @param classInstance The instance of the class to bind the methods to.
     */
    bindClassMethods(methods, classInstance) {
        methods.forEach(method => {
            classInstance[method] = classInstance[method].bind(classInstance);
        });
    }

    formatCurrency(amount) {
        const formatter = new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD',
        });
        return formatter.format(amount);
    }

    /**
     * Creates a div containing all elements to display book details.
     * @param book element from the datastore, which details are taken from.
     */
    createBookDiv(book) {
        let result = document.getElementById("book-container");

        while (result.firstChild) {
            result.removeChild(result.firstChild);
        }

        const bookDiv = document.createElement('div');
        const title = document.createElement('h3');
        title.innerText = book.title;

        bookDiv.appendChild(title);
        bookDiv.appendChild(document.createTextNode(`Released: ${book.releaseYear}`));
        bookDiv.appendChild(document.createElement('br'));
        bookDiv.appendChild(document.createTextNode(`Written by: ${book.writer}`));
        bookDiv.appendChild(document.createElement('br'));
        bookDiv.appendChild(document.createTextNode(`Illustrated by: ${book.illustrator}`));
        bookDiv.appendChild(document.createElement('br'));
        bookDiv.appendChild(document.createElement('br'));
        bookDiv.appendChild(document.createTextNode(book.description));

        result.appendChild(bookDiv);
    }

    showMessage(message) {
        Toastify({
            text: message,
            duration: 4500,
            gravity: "top",
            position: 'right',
            close: true,
            style: {
                background: "linear-gradient(to right, #00b09b, #96c93d)"
            }
        }).showToast();
    }

    errorHandler(error) {
        Toastify({
            text: error,
            duration: 4500,
            gravity: "top",
            position: 'right',
            close: true,
            style: {
                background: "linear-gradient(to right, rgb(255, 95, 109), rgb(255, 195, 113))"
            }
        }).showToast();
    }
}
