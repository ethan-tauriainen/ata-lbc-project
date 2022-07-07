import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import HomeClient from "../api/homeClient";

class AddPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['renderBookDetails', 'onAdd'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        document.getElementById('add-new-book-form').addEventListener('submit', this.onAdd);
        this.client = new HomeClient();

        this.dataStore.addChangeListener(this.renderBookDetails);
    }

    async renderBookDetails() {
        const book = this.dataStore.get("addedBook");

        if (book) {
            this.createBookDiv(book);
        }
    }

    async onAdd(event) {
        event.preventDefault();

        this.dataStore.set("addedBook", null);

        let createdBy = document.getElementById("add-book-details-createdBy").value;
        let releaseYear = document.getElementById("add-book-details-releaseYear").value;
        let title = document.getElementById("add-book-details-title").value;
        let writer = document.getElementById("add-book-details-writer").value;
        let illustrator = document.getElementById("add-book-details-illustrator").value;
        let description = document.getElementById("add-book-details-description").value;

        let request = {};
        request.createdBy = createdBy;
        request.releaseYear = releaseYear;
        request.title = title;
        request.writer = writer;
        request.illustrator = illustrator;
        request.description = description;

        const addedBook = await this.client.addNewBook(request, this.errorHandler);
          this.dataStore.set("addedBook", addedBook);

        if (addedBook) {
            this.showMessage(`Created ${addedBook.title}!`)
        } else {
            this.errorHandler("Error added book! Try again...");
            return;
        }
        document.getElementById('container').classList.add('show');
    }

}

const main = async () => {
    const addPage = new AddPage();
    await addPage.mount();
};

window.addEventListener('DOMContentLoaded', main);