config.devServer = {
    ...config.devServer, // Merge with other devServer settings
    "historyApiFallback": true,
    // "allowedHosts": "all" // insecure, use only for local testing
};
