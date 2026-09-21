

const BACKEND_URL = "https://libraai-library-management-system-jvy8.onrender.com/LibraryApp";
/* LOAD STATISTICS */

function loadStats() {

    fetch(BACKEND_URL + "/stats")

        .then(response => response.json())

        .then(data => {

            document.getElementById("totalBooks").textContent =
                data.totalBooks ?? 0;

            document.getElementById("availableBooks").textContent =
                data.availableCopies ?? 0;

            document.getElementById("totalRacks").textContent =
                data.totalRacks ?? 0;

        })

        .catch(error => {

            console.error("Stats error:", error);

        });
}


/* LOAD BOOKS */

function loadBooks() {

    fetch(BACKEND_URL + "/books")

        .then(response => response.json())

        .then(books => {

            const container =
                document.getElementById("bookContainer");

            container.innerHTML = "";

            if (!books.length) {

                container.innerHTML =
                    "<p>No books found.</p>";

                return;
            }


            books.forEach(book => {

                const card =
                    document.createElement("div");

                card.className = "book-card";

                card.innerHTML = `

                    <h3>${escapeHtml(book.title)}</h3>

                    <p>
                        <strong>Author:</strong>
                        ${escapeHtml(book.author)}
                    </p>

                    <p>
                        <strong>Category:</strong>
                        ${escapeHtml(book.category)}
                    </p>

                    <p>
                        <strong>Rack:</strong>
                        ${escapeHtml(book.rack_no)}
                    </p>

                    <p>
                        <strong>Shelf:</strong>
                        ${escapeHtml(book.shelf_no)}
                    </p>

                    <p>
                        <strong>Copies:</strong>
                        ${book.quantity}
                    </p>

                `;

                container.appendChild(card);

            });

        })

        .catch(error => {

            console.error("Books error:", error);

            document.getElementById("bookContainer").innerHTML =
                "<p>Unable to load books.</p>";

        });
}

/* TEXT TO SPEECH */
function speakBookDetails(book) {

    const text =
        "Book found. " +
        "Title: " + book.title + ". " +
        "Author: " + book.author + ". " +
        "Category: " + book.category + ". " +
        "Rack number: " + book.rack_no + ". " +
        "Shelf number: " + book.shelf_no + ". " +
        "Available copies: " + book.quantity + ".";

    const speech = new SpeechSynthesisUtterance(text);

    speech.rate = 0.9;
    speech.pitch = 1;
    speech.volume = 1;

    window.speechSynthesis.cancel();
    window.speechSynthesis.speak(speech);
}
/* SEARCH BOOKS */

function searchBooks() {

    const input =
        document.getElementById("searchInput");

    const query =
        input.value.trim();

    const result =
        document.getElementById("searchResult");


    if (!query) {

        result.innerHTML =
            "<p>Please enter a book title, author or category.</p>";

        return;
    }


    fetch(
        BACKEND_URL +
        "/search?q=" +
        encodeURIComponent(query)
    )

        .then(response => response.json())

        .then(data => {

            result.innerHTML = "";

            if (!data.length) {

                result.innerHTML =
                    "<p>No matching books found.</p>";

                return;
            }


            data.forEach(book => {

                const card =
                    document.createElement("div");

                card.className =
                    "search-result-card";

                card.innerHTML = `

                    <h3>${escapeHtml(book.title)}</h3>

                    <p>
                        Author:
                        ${escapeHtml(book.author)}
                    </p>

                    <p>
                        Category:
                        ${escapeHtml(book.category)}
                    </p>

                    <p>
                        Rack:
                        <strong>${escapeHtml(book.rack_no)}</strong>
                    </p>

                    <p>
                        Shelf:
                        <strong>${escapeHtml(book.shelf_no)}</strong>
                    </p>

                    <p>
                        Available Copies:
                        ${book.quantity}
                    </p>
<button onclick='speakBookDetails(${JSON.stringify(book)})'>
    🔊 Speak Details
</button>
                `;

                result.appendChild(card);

            });

        })

        .catch(error => {

            console.error("Search error:", error);

            result.innerHTML =
                "<p>Unable to search books.</p>";

        });
}


/* RACK FINDER */

function findRack() {

    const input =
        document.getElementById("rackInput");

    const query =
        input.value.trim();

    const result =
        document.getElementById("rackResult");


    if (!query) {

        result.innerHTML =
            "<p>Please enter a book name.</p>";

        return;
    }


    fetch(
        BACKEND_URL +
        "/search?q=" +
        encodeURIComponent(query)
    )

        .then(response => response.json())

        .then(data => {

            result.innerHTML = "";


            if (!data.length) {

                result.innerHTML =
                    "<p>Book not found.</p>";

                return;
            }


            const book = data[0];


            result.innerHTML = `

                <div class="rack-result-card">

                    <h3>${escapeHtml(book.title)}</h3>

                    <p>
                        <strong>Author:</strong>
                        ${escapeHtml(book.author)}
                    </p>

                    <p>
                        <strong>Category:</strong>
                        ${escapeHtml(book.category)}
                    </p>

                    <p>
                        <strong>Rack:</strong>
                        ${escapeHtml(book.rack_no)}
                    </p>

                    <p>
                        <strong>Shelf:</strong>
                        ${escapeHtml(book.shelf_no)}
                    </p>

                    <p>
                        <strong>Available Copies:</strong>
                        ${book.quantity}
                    </p>

                </div>

            `;

        })

        .catch(error => {

            console.error("Rack error:", error);

            result.innerHTML =
                "<p>Unable to find rack.</p>";

        });
}


/* BASIC HTML ESCAPING */

function escapeHtml(value) {

    if (value === null || value === undefined) {
        return "";
    }

    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}


/* START */

document.addEventListener("DOMContentLoaded", function () {

    loadStats();

    loadBooks();

});
