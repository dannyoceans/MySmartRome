<div class="row">
    <div class="col-md-4 col-md-offset-4">

        <div class="well bs-component">
<form class="form-horizontal" action="#" method="GET" >
  <fieldset>
    <legend>My smart Rome notification</legend>
    
<div class="form-group">
 <label for="inputEmail" class="col-lg-2 control-label">Name</label>
      <div class="col-lg-10">
        <input type="text" class="form-control" id="inputEmail" placeholder="Attraction name" name="title">
      </div>
      <label for="textArea" class="col-lg-2 control-label" name="body">Message</label>
     
      <div class="col-lg-10">
        <textarea class="form-control" rows="3" id="textArea" name="body" placeholder="insert here the message"></textarea>
	
        <input  type="hidden" name="lat" value="<?php echo $json_o['latitudine']; ?>" >
        <input  type="hidden" name="lng" value="<?php echo $json_o[longitudine]; ?>" > 
        
      </div>
 </div>

 
    <div class="form-group">
      <div class="col-lg-10 col-lg-offset-2">
        <button type="reset" class="btn btn-default">Cancel</button>
        <button type="submit" class="btn btn-primary" name="invia">Send</button>
      </div>
    </div>
  </fieldset>
</form>
    </div>
    </div>
</div>

