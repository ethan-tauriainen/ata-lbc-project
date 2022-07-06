import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import HomeClient from "../api/homeClient";

class DeletePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onSubmit'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        document.getElementById('delete-book-form').addEventListener('submit', this.onCreate);
        this.client = new HomeClient;

//        this.dataStore.addChangeListener(this.renderDelete)
    }


    async onSubmit(event) {
             // Prevent the page from refreshing on form submit
             event.preventDefault();

             let asin = document.getElementById("asin-field").value;
             let name = document.getElementById("name-field").value;

             const deleteBook = await this.client.deleteBook(asin, name, this.errorHandler);

             if(deleteBook) {
                this.showMessage(`Deleted Comic Book!`)
             } else {
                this.errorHandler("Error deleting! Try again...");
             }
         }

const main = async () => {
    const deletePage = new DeletePage();
    await deletePage.mount();
};
window.addEventListener('DOMContentLoaded', main);
}