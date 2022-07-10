import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import HomeClient from "../api/homeClient";

class DeletePage extends BaseClass {

    constructor() {
        super();
//        this.bindClassMethods(['onDelete', 'renderDelete'], this);
        this.bindClassMethods(['onDelete'], this);

        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers
     */
    async mount() {
        document.getElementById('delete-book-form').addEventListener('submit', this.onDelete);
        this.client = new HomeClient();
    }

    /** Event Handlers -----------------------------------------------------------------------------------------------*/

    async onDelete(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let name = document.getElementById("delete-book-name-field").value;
        let asin = document.getElementById("delete-book-asin-field").value;

        const book = await this.client.deleteBook(asin, name, this.errorHandler);

        if (book == true) {
            this.showMessage(`Deleted ${asin}!`);
            document.getElementById('delete-book-name-field').value = "";
            document.getElementById('delete-book-asin-field').value = "";
        } else {
            this.errorHandler("Error deleting! Try again...");
            document.getElementById('delete-book-name-field').value = "";
            document.getElementById('delete-book-asin-field').value = "";
        }
    }
}

const main = async () => {
    const deletePage = new DeletePage();
    deletePage.mount();
};

window.addEventListener('DOMContentLoaded', main);