$(document).ready(function() {
    $cog = $(".fa-cog"),
    $overlay = $(".profile__overlay"),
    $closeBtn = $(".fa-times"),
    $cancelBtn = $("#cancel"),
    $logoutBtn = $("#logout"); 


    $follower = $(".followerClick"),
    $overlay1 = $(".profile__overlay1"),
    $closeBtn1 = $(".fa-times"),
    $cancelBtn1 = $("#cancel"),
    $logoutBtn1 = $("#logout"); 


  $follower.click(function() {
    $overlay1.fadeIn();
  });

  $closeBtn1.click(closePopup);
  $cancelBtn1.click(closePopup);

   function closePopup() {
    $overlay1.fadeOut();
  }

  $logoutBtn1.click(function() {
    closePopup();
  });

  $cog.click(function() {
    $overlay.fadeIn();
  });


  $closeBtn.click(closePopup);
  $cancelBtn.click(closePopup);

   function closePopup() {
    $overlay.fadeOut();
  }

  $logoutBtn.click(function() {
    closePopup();
  });
});

