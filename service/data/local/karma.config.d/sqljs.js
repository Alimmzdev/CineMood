const path = require("path");
const os = require("os");
const sqlWasm = require.resolve("sql.js/dist/sql-wasm.wasm");

config.files.push({
    pattern: sqlWasm,
    served: true,
    watched: false,
    included: false,
});
config.proxies["/sql-wasm.wasm"] = `/absolute${sqlWasm}`;

// Serve Webpack's worker chunks alongside the SQL.js binary in browser tests.
config.webpack.output = {
    ...config.webpack.output,
    path: path.join(os.tmpdir(), `cinemood-karma-${process.pid}`),
};
config.webpack.resolve.fallback = {
    ...config.webpack.resolve.fallback,
    fs: false,
    path: false,
    crypto: false,
};
config.files.push({
    pattern: `${config.webpack.output.path}/**/*`,
    watched: false,
    included: false,
});
