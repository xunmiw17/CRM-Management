$(function () {
   // Loads the tree structure
   loadModuleData();
});

// Define a tree structure object
let zTreeObj;

/**
 * Loads resource tree list
 */
function loadModuleData() {
   // Info object config (zTree parameters config)
   let setting = {
      // Use checkbox
      check: {
         enable: true
      },
      // Use simple JSON data
      data: {
         simpleData: {
            enable: true
         }
      },
      callback: {
         // zTreeOnCheck Called when the checkbox/radio is checked
         onCheck: zTreeOnCheck
      }
   };

   // Use AJAX to query the resources list
   $.ajax({
      type: "get",
      url: "/crm" + "/module/queryAllModules?roleId=" + $("#roleId").val(),
      dataType: "json",
      success: function (data) {
         zTreeObj = $.fn.zTree.init($("#test1"), setting, data);
      }
   });
}

function zTreeOnCheck(event, treeId, treeNode) {
   let nodes = zTreeObj.getCheckedNodes(true);
   // Gets all resource ids
   let mIds;
   if (nodes.length > 0) {
      mIds = "mIds="
      for (let i = 0; i < nodes.length - 1; i++) {
         mIds += nodes[i].id + "&mIds="
      }
      mIds += nodes[nodes.length - 1].id;
   }

   let roleId = $("#roleId").val();

   $.ajax({
      type: "post",
      url: "/crm" + "/role/grant",
      data: mIds + "&roleId=" + roleId,
      dataType: "json",
      success: function (data) {
         console.log(data);
      }
   })
}