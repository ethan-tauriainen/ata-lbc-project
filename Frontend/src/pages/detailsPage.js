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
        let result = document.getElementById("container");

        const book = this.dataStore.get("book");

        if (book) {
            const bookDiv = document.createElement('div');
            const title = document.createElement('div');
            const description = document.createElement('div')
            const writer = document.createElement('div')
            const illustrator = document.createElement('div')
            const releaseYear = document.createElement('div')

            bookDiv.classList.add('book')
            bookDiv.setAttribute('id', book.title)

            title.textContent = book.title;
            title.classList.add('title');
            bookDiv.appendChild(title);

            releaseYear.textContent = `Released: ${book.releaseYear}`;
            releaseYear.classList.add('release-year');
            bookDiv.appendChild(releaseYear);

            writer.textContent = `Written by: ${book.writer}`;
            writer.classList.add('writer');
            bookDiv.appendChild(writer);

            illustrator.textContent = `Illustrated by: ${book.illustrator}`;
            illustrator.classList.add('illustrator');
            bookDiv.appendChild(illustrator);

            description.textContent = book.description;
            description.classList.add('description');
            bookDiv.appendChild(description);

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
        if (result) {
            this.showMessage(`Got ${result.title}!`)
        } else {
            this.errorHandler("Error doing GET! Try again...");
        }
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