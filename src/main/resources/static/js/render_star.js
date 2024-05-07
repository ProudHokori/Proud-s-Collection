function createStarRating(rating) {
    let stars = '';
    const fullStars = Math.floor(rating); // Full stars
    const halfStar = rating % 1 !== 0; // Check if there's a half star
    const grayStars = 5 - Math.ceil(rating); // Remaining gray stars

    // Add full stars
    for (let i = 0; i < fullStars; i++) {
        stars += '<i class="fa fa-star fa-2x" style="color:orange;"></i>';
    }

    // Add half star if needed
    if (halfStar) {
        stars += '<i class="fa fa-star-half-full fa-2x" style="color:orange;"></i>';
    }

    // Add gray stars
    for (let i = 0; i < grayStars; i++) {
        stars += '<i class="fa fa-star-o fa-2x" style="color:orange;"></i>';
    }

    return stars;
}

document.addEventListener("DOMContentLoaded", function() {
    const bookElements = document.querySelectorAll('.star-rating');
    bookElements.forEach(bookElement => {
        const averageRating = parseFloat(bookElement.textContent);
        console.log(averageRating);
        const starRating = createStarRating(averageRating);
        console.log(starRating);
        bookElement.innerHTML = DOMPurify.sanitize(starRating);;
    });
});

document.addEventListener("DOMContentLoaded", function() {
    const bookElement = document.getElementById('adminRating');
    const averageRating = parseFloat(bookElement.textContent);
    console.log(averageRating);
    const starRating = createStarRating(averageRating);
    console.log(starRating);
    bookElement.innerHTML = starRating;
});

document.addEventListener("DOMContentLoaded", function() {
    const bookElement = document.getElementById('avgUsrRating');
    const averageRating = parseFloat(bookElement.textContent);
    console.log(averageRating);
    const starRating = createStarRating(averageRating);
    console.log(starRating);
    bookElement.innerHTML = starRating;
});