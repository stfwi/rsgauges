#!/usr/bin/djs
if((!fs.chdir(fs.dirname(fs.realpath(sys.script))+"/..")) || (!fs.isdir("src"))) throw new Error("Failed to switch to mod source directory.");

var file_list = (function() {
  var ls = [];
  ls = fs.find("./src/main/resources/assets/rsgauges/models/block", '*.json');
  for(var i in ls) ls[i] = ls[i].replace(/\\/g,"/");
  ls.sort();
  return ls;
})();

var errors = [];
for(var file_i in file_list) {
  var file = file_list[file_i];
  var txt = fs.readfile(file);
  if(txt===undefined) throw new Error("Failed to read '" + file + "'");
  var model = null;
  try { model = JSON.parse(txt); } catch(ex) { throw new Error("Failed to parse JSON of '" + file + "'"); }
  const add_error = function(msg) {errors.push("File '" + file + "': " + msg);}
  if(model.parent === undefined) add_error("Missing parent");
  if(model.textures === undefined) add_error("Missing textures");
  if(model.display === undefined) {
    if((model.parent=== undefined) || (model.parent.search("cube_all")<0)) add_error("Missing display");
  } else {
    if(model.display.gui === undefined) add_error("Missing display.gui");
    if(model.display.fixed === undefined) add_error("Missing display.fixed");
    if(model.display.ground === undefined) add_error("Missing display.ground");
  }
}
for(var i in errors) alert(errors[i]);
if(errors.length > 0) exit(1);
