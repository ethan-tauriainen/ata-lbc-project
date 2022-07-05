import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import HomeClient from "../api/homeClient";

class UpdatePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['renderBookDetails', 'onUpdate'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        document.getElementById('update-book-details-form').addEventListener('submit', this.onUpdate);
        this.client = new HomeClient();

        this.dataStore.addChangeListener(this.renderBookDetails);
    }

    async renderBookDetails() {
        const book = this.dataStore.get("updatedBook");

        if (book) {
            this.createBookDiv(book);
        }
    }

    async onUpdate(event) {
        event.preventDefault();

        let asin = document.getElementById("update-book-details-asin").value;

        this.dataStore.set("bookFromBackend", null);
        const bookFromBackend = await this.client.findBookByAsin(asin, this.errorHandler);

        if (!bookFromBackend) {
            this.errorHandler("Error finding book! Try again...");
            this.resetValues();
            return;
        }

        this.dataStore.set("bookFromBackend", bookFromBackend);

        let createdBy = document.getElementById("update-book-details-createdBy").value;
        let releaseYear = document.getElementById("update-book-details-releaseYear").value;
        let title = document.getElementById("update-book-details-title").value;
        let writer = document.getElementById("update-book-details-writer").value;
        let illustrator = document.getElementById("update-book-details-illustrator").value;
        let description = document.getElementById("update-book-details-description").value;

        let request = {};
        request.asin = asin;
        request.createdBy = createdBy;
        request.releaseYear = releaseYear.length > 0 ? releaseYear : bookFromBackend.releaseYear;
        request.title = title.length > 0 ? title : bookFromBackend.title;
        request.writer = writer.length > 0 ? writer : bookFromBackend.writer;
        request.illustrator = illustrator.length > 0 ? illustrator : bookFromBackend.illustrator;
        request.description = description.length > 0 ? description : bookFromBackend.description;

        this.dataStore.set("updatedBook", null);
        const updatedBook = await this.client.updateComicBook(request, this.errorHandler);

        if (!updatedBook) {
            this.errorHandler("Error updating book! Try again...");
            this.resetValues();
            return;
        }

        this.dataStore.set("updatedBook", updatedBook);
        document.getElementById('container').classList.add('show');
        this.resetValues();
    }

    resetValues() {
        document.getElementById("update-book-details-asin").value = "";
        document.getElementById("update-book-details-createdBy").value = "";
        document.getElementById("update-book-details-releaseYear").value = "";
        document.getElementById("update-book-details-title").value = "";
        document.getElementById("update-book-details-writer").value = "";
        document.getElementById("update-book-details-illustrator").value = "";
        document.getElementById("update-book-details-description").value = "";
    }

}

const main = async () => {
    const updatePage = new UpdatePage();
    await updatePage.mount();
};

window.addEventListener('DOMContentLoaded', main);