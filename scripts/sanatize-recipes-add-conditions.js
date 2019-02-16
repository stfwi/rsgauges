#!/usr/bin/djs
if((!fs.chdir(fs.dirname(fs.realpath(sys.script))+"/..")) || (!fs.isdir("src"))) throw new Error("Failed to switch to mod source directory.");

var file_list = (function() {
  var ls = [];
  ls = fs.find("./src/main/resources/assets/rsgauges/recipes", '*.json');
  for(var i in ls) {
    ls[i] = ls[i].replace(/\\/g,"/");
    if(fs.basename(ls[i]).indexOf("_")===0) ls[i] = null;
  }
  ls = ls.filter(function(e){return e!==null;});
  ls.sort();
  return ls;
})();

function getjson(txt) {
  try { return JSON.parse(txt); } catch(ex) { return undefined; }
}

function compose_json(recipe) {
  var s = JSON.stringify(recipe, null, 2);
  return s;
}

var errors = [];

for(var file_i in file_list) {
  try {
    var file = file_list[file_i];
    var json = fs.readfile(file);
    var recipe = getjson(json);
    if(recipe===undefined) throw new Error("Failed to read or parse '" + file + "'.");
    if(!fs.writefile(file, compose_json(recipe))) throw new Error("Failed to write or compose '" + file + "'.");
  } catch(ex) {
    errors.push(""+ex);
  }
}

if(errors.length > 0) {
  alert("Recipe sanatizing errors:");
  for(var i in errors) alert(" - " + errors[i]);
  exit(1);
}
