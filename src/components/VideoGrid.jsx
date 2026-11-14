import VideoCard from './VideoCard';

export default function VideoGrid({ videos, title }) {
  return (
    <div>
      {title && (
        <h2 className="text-xl font-semibold mb-4 px-1">{title}</h2>
      )}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5 gap-x-4 gap-y-8">
        {videos.map((video) => (
          <VideoCard key={video.id} video={video} />
        ))}
      </div>
    </div>
  );
}