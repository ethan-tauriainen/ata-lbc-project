 import BaseClass from "../util/baseClass";
 import DataStore from "../util/DataStore";
 import HomeClient from "../api/homeClient";

 /**
  * Logic needed for the view playlist page of the website.
  */
 class HomePage extends BaseClass {

     constructor() {
         super();
         this.bindClassMethods(['onGetComicBooks', 'onCreate', 'renderComicBooks'], this);
         this.dataStore = new DataStore();
     }

     /**
      * Once the page has loaded, set up the event handlers and fetch the concert list.
      */
     async mount() {
//         document.getElementById('get-comic-books-form').addEventListener('submit', this.onGet);
         document.getElementById('get-all-comic-books-form').addEventListener('submit', this.onCreate);
         this.client = new HomeClient();
         await this.onGetComicBooks();

         this.dataStore.addChangeListener(this.renderComicBooks)
     }

     // Render Methods --------------------------------------------------------------------------------------------------

     async renderComicBooks() {
         let resultArea = document.getElementById("all-books-info");

         const comicBooks = this.dataStore.get("comicBooks");

         let html = ""
         html += "<ul>"

         for(let comicBook of comicBooks) {
            html += `
                <li>
                    <h3>${comicBook.title}</h3>
                    <p>Asin: ${comicBook.asin}</p>
                 </li>
            `
         }

         html += "</ul>"

        if(comicBooks) {
           resultArea.innerHTML = html;
        } else {
           resultArea.innerHTML = "No Comic Books";
        }
     }

     // Event Handlers --------------------------------------------------------------------------------------------------

     async onGetComicBooks() {
         // Prevent the page from refreshing on form submit
         let result = await this.client.getAllComicBooks(this.errorHandler);
         this.dataStore.set("comicBooks", result);
     }

     async onCreate(event) {
         // Prevent the page from refreshing on form submit
         event.preventDefault();
         await this.onGetComicBooks();
     }
 }

 /**
  * Main method to run when the page contents have loaded.
  */
 const main = async () => {
     const homePage = new HomePage();
     await homePage.mount();
 };

 window.addEventListener('DOMContentLoaded', main);
