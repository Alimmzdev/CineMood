const screenshots = {
  compose: [['home-compose', 'Discover'], ['movie-detail-compose', 'Movie details'], ['favorite-list-compose', 'Favorites'], ['setting-compose', 'Make it yours']],
  ios: [['home-ios', 'Discover'], ['movie-details-ios', 'Movie details'], ['search-ios', 'Search'], ['favorite-ios', 'Favorites'], ['setting-ios', 'Make it yours']]
};
const gallery = document.querySelector('#gallery');
const description = document.querySelector('#gallery-description');
for (const button of document.querySelectorAll('[data-platform]')) {
  button.addEventListener('click', () => {
    const platform = button.dataset.platform;
    for (const item of document.querySelectorAll('[data-platform]')) item.setAttribute('aria-pressed', String(item === button));
    gallery.replaceChildren(...screenshots[platform].map(([file, title], index) => {
      const figure = document.createElement('figure');
      const link = document.createElement('a');
      link.href = `assets/${file}.jpg`;
      const img = document.createElement('img');
      img.src = link.href;
      img.alt = `${platform === 'ios' ? 'Native iOS' : 'Compose'} ${title} screen`;
      img.width = 640;
      img.height = platform === 'ios' ? 1391 : 1436;
      img.loading = 'lazy';
      link.append(img);
      const caption = document.createElement('figcaption');
      const number = document.createElement('span');
      number.textContent = String(index + 1).padStart(2, '0');
      caption.append(number, title);
      figure.append(link, caption);
      return figure;
    }));
    gallery.scrollLeft = 0;
    gallery.setAttribute('aria-label', `${platform === 'ios' ? 'Native iOS' : 'Compose'} screenshots`);
    description.textContent = platform === 'ios' ? 'A native SwiftUI experience, powered by the same shared Kotlin logic.' : 'The shared Compose interface, from discovery to your saved collection.';
  });
}
