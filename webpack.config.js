var path = require('path');
var webpack = require('webpack');

module.exports = {
    entry: './src/main.js',
    output: { path: __dirname + "/dist", filename: 'bundle.js' },
    module: {
        loaders: [
            {
                test: /.js$/,
                loader: 'babel-loader',
                exclude: /node_modules/,
                query: {
                    presets: ['es2015', 'es2016', 'es2017']
                }
            },
            {
                test: /.handlebars$/,
                loader:'handlebars-loader',
                exclude: /node_modules/
            }
        ]
    },
};