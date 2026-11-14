import { videos } from '../data/videos';
import VideoGrid from '../components/VideoGrid';

export default function Home() {
  return (
    <div className="p-4 md:p-6">
      <div className="flex gap-3 overflow-x-auto pb-4 mb-2 scrollbar-hide">
        {['All', 'Music', 'Gaming', 'Live', 'News', 'Sports', 'Learning', 'Fashion'].map(
          (tag) => (
            <button
              key={tag}
              className={`px-3 py-1.5 rounded-lg text-sm whitespace-nowrap transition ${
                tag === 'All'
                  ? 'bg-white text-black font-medium'
                  : 'bg-tube-card hover:bg-tube-hover'
              }`}
            >
              {tag}
            </button>
          )
        )}
      </div>
      <VideoGrid videos={videos} />
    </div>
  );
}