config.resolve.fallback = {
    ...config.resolve.fallback,
    fs: false,
    path: false,
    crypto: false,
};

const CopyWebpackPlugin = require("copy-webpack-plugin");
config.plugins.push(new CopyWebpackPlugin({
    patterns: [{
        from: require.resolve("sql.js/dist/sql-wasm.wasm"),
        to: "sql-wasm.wasm",
    }],
}));
