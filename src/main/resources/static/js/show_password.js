document.addEventListener("DOMContentLoaded", function() {
  document.querySelectorAll(".toggle-password").forEach(function(element) {
    element.addEventListener("click", function() {
      console.log("Eye icon clicked");
      this.classList.toggle("fa-eye");
      this.classList.toggle("fa-eye-slash");
      var input = document.querySelector(this.getAttribute("toggle"));
      if (input.getAttribute("type") === "password") {
        input.setAttribute("type", "text");
      } else {
        input.setAttribute("type", "password");
      }
    });
  });
});
