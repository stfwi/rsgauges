#!/usr/bin/djs
// Note for reviewers/clones: This file is a auxiliary script for my setup. It's not needed to build the mod.
"use strict";
const constants  = include("../meta/lib/constants.js")();
constants.options.without_ref_repository_check = true;
const libtask    = include("../meta/lib/libtask.js")(constants);
const liblang    = include("../meta/lib/liblang.1.13.js")(constants); // 1.14 lang same as 1.13
const libassets  = include("../meta/lib/libassets.js")(constants);
const libtask114 = include("../meta/lib/libtask.1.14.js")(constants, libassets, liblang);
var tasks = {};

tasks["sync-languages"] = function() {
  liblang.sync_languages();
};

tasks["assets"] = function() {
  libtask114.stdtasks["assets"]();
};

libtask.run(tasks, sys.args);
