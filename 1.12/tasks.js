#!/usr/bin/djs
// Note for reviewers/clones: This file is a auxiliary script for my setup. It's not needed to build the mod.
"use strict";
const constants = include("../meta/lib/constants.js")();
const libtask = include("../meta/lib/libtask.js")(constants);
const liblang12 = include("../meta/lib/liblang.1.12.js")(constants);
const liblang14 = include("../meta/lib/liblang.1.13.js")(constants);
var tasks = {};

tasks["sync-languages"] = function() {
  liblang12.sync_languages();
};

tasks["port-languages-from-112"] = function() {
  fs.find("src/main/resources/assets/"+ constants.mod_registry_name() +"/lang", '*.lang', function(path){
    const unified = liblang12.load(path);
    path = path.replace(/\.lang$/,"");
    var unified_remapped = JSON.stringify(unified);
    if(constants.registryname_map_112_114 !== undefined) {
      const keys = Object.keys(constants.registryname_map_112_114);
      keys.sort(function(a,b){return a.length>b.length ? -1 : 1;});
      for(var i in keys) {
        var oldname = keys[i];
        var newname = constants.registryname_map_112_114[oldname];
        unified_remapped = unified_remapped.split("${tile.").join("${block.");
        unified_remapped = unified_remapped.split('.'+oldname+'.').join('.'+newname+'.');
        unified_remapped = unified_remapped.split('.'+oldname+'"').join('.'+newname+'"');
        unified_remapped = unified_remapped.split('"'+oldname+'":').join('"'+newname+'":');
      }
    }
    unified_remapped = unified_remapped.split("\\\\n").join("\\n");
    unified_remapped = unified_remapped.split("\\n ").join("\\n");
    unified_remapped = JSON.parse(unified_remapped);
    liblang14.save("../1.14/"+path+".json", unified_remapped);
    return false;
  });
};

libtask.run(tasks, sys.args);
