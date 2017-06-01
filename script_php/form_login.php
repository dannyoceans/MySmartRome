<form class="form-inline navbar-right " style="padding:10px" role="search" method="POST" action=<?php $_SERVER['php.self'];?>>
  <label class="sr-only" for="inlineFormInputGroup">Username</label>
  <div class="input-group mb-2 mr-sm-2 mb-sm-0">
    <div class="input-group-addon">@</div>
    <input type="text" class="form-control" id="inlineFormInputGroup" placeholder="Username" name="user">
  </div>
  <label class="sr-only" for="inlineFormInput">Name</label>
  <input type="password" class="form-control mb-2 mr-sm-2 mb-sm-0" id="inlineFormInput" placeholder="password" name="pswd">

  <button type="submit" class="btn btn-primary" name="login">Login</button>
</form>