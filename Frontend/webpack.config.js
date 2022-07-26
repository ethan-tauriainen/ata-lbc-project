const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

module.exports = {
  optimization: {
    usedExports: true
  },
  entry: {
    homePage: path.resolve(__dirname, 'src', 'pages', 'homePage.js'),
    detailsPage: path.resolve(__dirname, 'src', 'pages', 'detailsPage.js'),
    updatePage: path.resolve(__dirname, 'src', 'pages', 'updatePage.js'),
    addPage: path.resolve(__dirname, 'src', 'pages', 'addPage.js'),
    deletePage: path.resolve(__dirname, 'src', 'pages', 'deletePage.js')
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: '[name].js',
  },
  devServer: {
    https: true,
    port: 8080,
    open: true,
    openPage: 'https://localhost:8080',
    // disableHostChecks, otherwise we get an error about headers and the page won't render
    disableHostCheck: true,
    contentBase: 'packaging_additional_published_artifacts',
    // overlay shows a full-screen overlay in the browser when there are compiler errors or warnings
    overlay: true,
    proxy: [
      {
        context: [
          '/books'
        ],
        target: 'http://localhost:5001'
      }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './src/index.html',
      filename: 'index.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/detailsPage.html',
      filename: 'detailsPage.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/updatePage.html',
      filename: 'updatePage.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/addPage.html',
      filename: 'addPage.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/deletePage.html',
      filename: 'deletePage.html',
      inject: false
    }),
    new CopyPlugin({
      patterns: [
        {
          from: path.resolve('src/css'),
          to: path.resolve("dist/css")
        }
      ]
    }),
    new CleanWebpackPlugin()   
  ]
}
