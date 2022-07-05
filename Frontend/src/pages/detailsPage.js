import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import HomeClient from "../api/homeClient";

class DetailsPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['renderBookDetails', 'onGet'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        document.getElementById('get-book-details-form').addEventListener('submit', this.onGet);
        this.client = new HomeClient();

        this.dataStore.addChangeListener(this.renderBookDetails)
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderBookDetails() {
        let result = document.getElementById("book-container");

        while (result.firstChild) {
            result.removeChild(result.firstChild);
        }

        const book = this.dataStore.get("book");

        if (book) {
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
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onGet(event) {
        event.preventDefault();

        let asin = document.getElementById("get-book-details-asin").value;
        this.dataStore.set("book", null);

        let result = await this.client.findBookByAsin(asin, this.errorHandler);
        this.dataStore.set("book", result);

        if (!result) {
            this.errorHandler("Error doing GET! Try again...");
            document.getElementById('get-book-details-asin').value = "";
            return;
        }

        document.getElementById('get-book-details-asin').value = "";

        const container = document.getElementById('container');
        container.classList.add('show');
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const detailsPage = new DetailsPage();
    await detailsPage.mount();
};

window.addEventListener('DOMContentLoaded', main);