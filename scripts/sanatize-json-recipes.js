#!/usr/bin/djs
if((!fs.chdir(fs.dirname(fs.realpath(sys.script))+"/..")) || (!fs.isdir("src"))) throw new Error("Failed to switch to mod source directory.");

function load_file_list() {
  var ls = [];
  ls = fs.find("./src/main/resources/assets/rsgauges/recipes", '*.json');
  for(var i in ls) {
    ls[i] = ls[i].replace(/\\/g,"/");
    if(fs.basename(ls[i]).indexOf("_")===0) ls[i] = null;
  }
  ls = ls.filter(function(e){return e!==null;});
  ls.sort();
  return ls;
};

function getjson(txt) {
  try { return JSON.parse(txt); } catch(ex) { return undefined; }
}

function compose_json(recipe) {
  var s = JSON.stringify(recipe, null, 2);
  return s;
}

function add_global_mod_condition(recipe) {
  var cond = { type:"rsgauges:grc" };
  if(recipe.result.item.indexOf("rsgauges:")==0) { cond.result = recipe.result.item; }
  var needed_items = {};

  if(recipe.key !== undefined) {
    for(var k in recipe.key) {
      if(recipe.key[k].item===undefined) continue;
      var s = recipe.key[k].item;
      if(s.indexOf("rsgauges:")!=0) continue;
      needed_items[s]=s;
    }
  }
  if(recipe.ingredients !== undefined) {
    for(var k in recipe.ingredients) {
      if(recipe.ingredients[k].item===undefined) continue;
      var s = recipe.ingredients[k].item;
      if(s.indexOf("rsgauges:")!=0) continue;
      needed_items[s]=s;
    }
  }

  needed_items = Object.keys(needed_items);
  if(needed_items.length > 0) {
    cond.items = needed_items;
  } else if(cond.items !== undefined) {
    delete cond.items;
  }
  recipe.conditions = [cond];
  return recipe;
}

var errors = [];
var file_list = load_file_list();
for(var file_i in file_list) {
  try {
    var file = file_list[file_i];
    var json = fs.readfile(file);
    var recipe = getjson(json);
    if(recipe===undefined) throw new Error("Failed to read or parse '" + file + "'.");
    recipe = add_global_mod_condition(recipe);
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
