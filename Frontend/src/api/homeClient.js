import BaseClass from "../util/baseClass";
import axios from 'axios'

/**
 * Client to call the MusicPlaylistService.
 *
 * This could be a great place to explore Mixins. Currently, the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
 */
export default class HomeClient extends BaseClass {

    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'getAllComicBooks', 'addNewBook', 'deleteBook'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */
    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
    }

    /**
     * Gets all Comic Books.
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns List of comic books.
     */
    async getAllComicBooks(errorCallback) {
        try {
            const response = await this.client.get(`/books/all`);
            return response.data;
        } catch (error) {
            this.handleError("getAllComicBooks", error, errorCallback)
        }
    }

    /**
     * Gets a comic book based on its ASIN.
     * @param asin the unique identifier of the comic book.
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns a single comic book.
     */
    async findBookByAsin(asin, errorCallback) {
        try {
            const response = await this.client.get(`/books/${asin}`);
            return response.data;
        } catch (error) {
            this.handleError("findBookByAsin", error, errorCallback)
        }
    }
    /**
     * Updates a comic book based on its ASIN and createdBy parameters.
     * @param request the request object containing values to update.
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns the updated entity.
     */
    async updateComicBook(request, errorCallback) {
        try {
            const response = await this.client.put(`/books`, {
                asin: request.asin,
                createdBy: request.createdBy,
                releaseYear: request.releaseYear,
                title: request.title,
                writer: request.writer,
                illustrator: request.illustrator,
                description: request.description
            });
            return response.data;
        } catch (error) {
            this.handleError("updateComicBook", error, errorCallback);
        }
    }
    /**
     * Adds a new comic book to the collection.
     * @param request the request object containing values to add.
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns the added entity.
     */
    async addNewBook(request, errorCallback) {
        try {
            const response = await this.client.post(`/books`, {
                createdBy: request.createdBy,
                releaseYear: request.releaseYear,
                title: request.title,
                writer: request.writer,
                illustrator: request.illustrator,
                description: request.description
            });
                return response.data;
            } catch (error) {
                this.handleError("addNewBook", error, errorCallback);
            }
    }
    /**
     * Delete an existing comic book in the collection.
     * @param asin the unique identifier of the comic book.
     * @param name of the user who created comic book entry
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    async deleteBook(asin, name, errorCallback) {
        try {
            const response = await this.client.delete(`/books/delete/${asin}/createdBy/${name}`)
            return response.data;
        } catch (error) {
            this.handleError("deleteBook", error, errorCallback);
            }
    }
    /**
     * Helper method to log the error and run any error functions.
     * @param method The method being called.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
}
