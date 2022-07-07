import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import HomeClient from "../api/homeClient";

class DeletePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onDelete', 'renderDelete'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers
     */
    async mount() {
        document.getElementById('delete-book-form').addEventListener('submit', this.onDelete);
        this.client = new HomeClient();

        this.dataStore.addChangeListener(this.renderDelete)
    }

    /** Render Methods -----------------------------------------------------------------------------------------------*/

    async renderDelete() {
        const deletedBook = this.dataStore.get("deletedBook");

        if (deletedBook) {
            this.createBookDiv(deletedBook);
        }
    }

    /** Event Handlers -----------------------------------------------------------------------------------------------*/

    async onDelete(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        this.dataStore.set("deletedBook", null);

        let name = document.getElementById("delete-book-name-field").value;
        let asin = document.getElementById("delete-book-asin-field").value;

        const deletedBook = await this.client.deleteBook(asin, name, this.errorHandler);
        this.dataStore.set("deletedBook", deletedBook);

        if (!deletedBook) {
            this.showMessage(`Deleted ${asin}!`);
            document.getElementById('delete-book-name-field').value = "";
            document.getElementById('delete-book-asin-field').value = "";
        } else {
            this.errorHandler("Error deleting!  Try again...");
        }
    }
}

const main = async () => {
    const deletePage = new DeletePage();
    deletePage.mount();
};

window.addEventListener('DOMContentLoaded', main);