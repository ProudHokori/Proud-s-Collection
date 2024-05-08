function createStarRating(rating) {
    let stars = [];
    const fullStars = Math.floor(rating); // Full stars
    const halfStar = rating % 1 !== 0; // Check if there's a half star
    const grayStars = 5 - Math.ceil(rating); // Remaining gray stars

    // Add full stars
    for (let i = 0; i < fullStars; i++) {
        let starElement = document.createElement('i');
        starElement.className = "fa fa-star fa-2x";
        starElement.style.color = "orange";
        stars.push(starElement.outerHTML);
    }

    // Add half star if needed
    if (halfStar) {
        let starElement = document.createElement('i');
        starElement.className = "fa fa-star-half-full fa-2x";
        starElement.style.color = "orange";
        stars.push(starElement.outerHTML);
    }

    // Add gray stars
    for (let i = 0; i < grayStars; i++) {
         let starElement = document.createElement('i');
         starElement.className = "fa fa-star-o fa-2x";
         starElement.style.color = "orange";
         stars.push(starElement.outerHTML);
    }

    return stars;
}

document.addEventListener("DOMContentLoaded", function() {
    const bookElements = document.querySelectorAll('.star-rating');
    bookElements.forEach(bookElement => {
        const averageRating = parseFloat(bookElement.textContent);
        const starRating = createStarRating(averageRating).join('');
        bookElement.innerHTML = starRating;
    });
});

document.addEventListener("DOMContentLoaded", function() {
    const adminRating = document.getElementById('adminRating');
    const averageRating = parseFloat(bookElement.textContent);
    const starRating = createStarRating(averageRating).join('');
    adminRating.innerHTML = starRating;
});

document.addEventListener("DOMContentLoaded", function() {
    const avgUsrRating = document.getElementById('avgUsrRating');
    const averageRating = parseFloat(bookElement.textContent);
    const starRating = createStarRating(averageRating).join('');
    avgUsrRating.innerHTML = starRating;
});