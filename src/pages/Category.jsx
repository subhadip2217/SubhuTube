import { useParams } from 'react-router-dom';
import { videos } from '../data/videos';
import VideoGrid from '../components/VideoGrid';

const CATEGORY_MAP = {
  music: {
    title: 'Music',
    filter: (v) =>
      /music|song|official|vevo|astley|sheeran|adele|maroon|katy|one republic|queen|ronson|psy|fonsi|wiz/i.test(
        v.title + v.channel
      ),
  },
  gaming: {
    title: 'Gaming',
    filter: () => true, // placeholder – show all for now
  },
  movies: {
    title: 'Movies',
    filter: () => true,
  },
  news: {
    title: 'News',
    filter: () => true,
  },
  explore: {
    title: 'Explore',
    filter: () => true,
  },
  shorts: {
    title: 'Shorts',
    filter: () => true,
  },
  subscriptions: {
    title: 'Subscriptions',
    filter: () => true,
  },
  history: {
    title: 'History',
    filter: () => true,
  },
  liked: {
    title: 'Liked videos',
    filter: () => true,
  },
  'watch-later': {
    title: 'Watch later',
    filter: () => true,
  },
};

export default function Category() {
  const { name } = useParams();
  const cat = CATEGORY_MAP[name] || { title: name, filter: () => true };
  const filtered = videos.filter(cat.filter);

  return (
    <div className="p-4 md:p-6">
      <h1 className="text-2xl font-semibold mb-6 capitalize">{cat.title}</h1>
      {filtered.length > 0 ? (
        <VideoGrid videos={filtered} />
      ) : (
        <p className="text-tube-muted">No videos in this category yet.</p>
      )}
    </div>
  );
}