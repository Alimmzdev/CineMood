const path = require("path");

module.exports = {
    devServer: {
        static: {
            directory: path.join(__dirname, "build/processedResources/js/main"),
        },
        compress: true,
        port: 8080,
        headers: {
            "Access-Control-Allow-Origin": "*",
        },
        proxy: [
            {
                context: ["/api"],
                target: "https://moviesapi.ir",
                changeOrigin: true,
                pathRewrite: { "^/api": "" },
            },
            {
                context: ["/images"],
                target: "https://moviesapi.ir",
                changeOrigin: true,
            },
        ],
    },
};
